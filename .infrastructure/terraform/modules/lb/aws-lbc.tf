resource "aws_iam_role" "lb_role" {
  name = "lb_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "pods.eks.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_policy" "lb_policy" {
  name = "AWSLoadBalancerControllerIAMPolicy"
  policy = file("${path.module}/policies/lb_policy.json")
}

resource "aws_iam_role_policy_attachment" "lb_policy_attachment" {
  policy_arn = aws_iam_policy.lb_policy.arn
    role       = aws_iam_role.lb_role.name
}

resource "aws_eks_pod_identity_association" "lb_eks_pod_identity_association" {
  cluster_name    = var.eks_name
  namespace       = "kube-system"
  service_account = "aws-load-balancer-controller"
  role_arn        = aws_iam_role.lb_role.arn
}

resource "helm_release" "lbc_release" {
  name  = "lbc_release"

  repository = "https://aws.github.io/eks-charts"
  chart      = "aws-load-balancer-controller"
  namespace = "kube-system"

  set {
    name  = "clusterName"
    value = var.eks_name
  }

  set {
    name  = "serviceAccount.create"
    value = "true"
  }

  set {
    name  = "serviceAccount.name"
    value = "aws-load-balancer-controller"
  }
}