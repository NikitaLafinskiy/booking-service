data "aws_iam_policy_document" "ebs_csi_driver_policy" {
  statement {
    effect = "Allow"

    principals {
      type        = "Service"
      identifiers = ["pods.eks.amazonaws.com"]
    }

    actions = [
      "sts:AssumeRole",
      "sts:TagSession"
    ]
  }
}

resource "aws_iam_role" "ebs_csi_driver_role" {
  name               = "ebs_csi_driver_role"
  assume_role_policy = data.aws_iam_policy_document.ebs_csi_driver_policy.json
}

resource "aws_iam_role_policy_attachment" "ebs_csi_driver_policy_attachment" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy"
  role       = aws_iam_role.ebs_csi_driver_role.name
}

resource "aws_iam_policy" "ebs_encryption_policy" {
  name        = "ebs_encryption_policy"

  policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": [
          "kms:CreateGrant",
          "kms:ListGrants",
          "kms:RevokeGrant"
        ],
        "Resource": ["custom-key-arn"],
        "Condition": {
          "Bool": {
            "kms:GrantIsForAWSResource": "true"
          }
        }
      },
      {
        "Effect": "Allow",
        "Action": [
          "kms:Encrypt",
          "kms:Decrypt",
          "kms:ReEncrypt*",
          "kms:GenerateDataKey*",
          "kms:DescribeKey"
        ],
        "Resource": ["custom-key-arn"]
      }
    ]
  })
}

resource "aws_iam_policy_attachment" "ebs_encryption_policy_attachment" {
  name       = "ebs_encryption_policy_attachment"
  roles      = [aws_iam_role.ebs_csi_driver_role.name]
  policy_arn = aws_iam_policy.ebs_encryption_policy.arn
}

resource "aws_eks_pod_identity_association" "ebs_csi_driver_identity" {
  cluster_name    = var.eks_name
  namespace       = "kube-system"
  service_account = "ebs-csi-controller-sa"
  role_arn        = aws_iam_role.ebs_csi_driver_role.arn
}

resource "aws_eks_addon" "ebs_csi_driver" {
  cluster_name                = var.eks_name
  addon_name                  = "aws-ebs-csi-driver"
  addon_version               = "v1.38.1-eksbuild.1"
}