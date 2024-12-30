locals {
  region = "eu-central-1"
  environment = "dev"
  zone1 = "eu-central-1a"
  zone2 = "eu-central-1b"
  eks_name = "booking-service-eks-${local.environment}"
  k8s_version = "1.31"
}