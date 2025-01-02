data "aws_caller_identity" "current" {}

resource "aws_iam_role" "admin_role" {
  name = "${var.environment}-${var.eks_name}-admin-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          AWS = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:root"
        }
      },
    ]
  })
}

resource "aws_iam_policy" "eks_admin_policy" {
  name        = "${var.environment}-${var.eks_name}-eks-admin-policy"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "eks:*",
        ]
        Effect   = "Allow"
        Resource = "*"
      },
      {
        Action = [
          "iam:PassRole",
        ],
        Resource = "*",
        Effect   = "Allow",
        Condition = {
          StringEquals = {
            "iam:PassedToService": "eks.amazonaws.com"
          }
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "eks_admin_role_attachment" {
  role       = aws_iam_role.admin_role.name
  policy_arn = aws_iam_policy.eks_admin_policy.arn
}

resource "aws_iam_user" "admin_user" {
  name = "admin"
}

resource "aws_iam_policy" "eks_assume_admin_policy" {
  name        = "eks_admin_policy"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "sts:AssumeRole",
        ]
        Effect   = "Allow"
        Resource = aws_iam_role.admin_role.arn
      },
    ]
  })
}

resource "aws_iam_user_policy_attachment" "eks_admin_policy_attachment" {
  user       = aws_iam_user.admin_user.name
  policy_arn = aws_iam_policy.eks_admin_policy.arn
}

resource "aws_eks_access_entry" "admin_entry" {
  cluster_name      = aws_eks_cluster.eks.name
  principal_arn     = aws_iam_role.admin_role.arn
  kubernetes_groups = [var.k8s_rbac_admin_group_name]
}