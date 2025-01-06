resource "aws_iam_role" "lb_role" {
  name = "lb_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "sts:AssumeRole",
          "sts:TagSession"
        ]
        Effect = "Allow"
        Principal = {
          Service = "pods.eks.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_policy" "lb_policy" {
  name = "AWSLoadBalancerController"
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
  name  = "aws-load-balancer-controller"

  repository = "https://aws.github.io/eks-charts"
  chart      = "aws-load-balancer-controller"
  namespace = "kube-system"
  version    = "1.8.1"

  set {
    name  = "clusterName"
    value = var.eks_name
  }

  set {
    name  = "serviceAccount.name"
    value = "aws-load-balancer-controller"
  }

  set {
    name  = "vpcId"
    value = var.vpc_id
  }
}