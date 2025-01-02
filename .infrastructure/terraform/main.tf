module "network" {
  source      = "./modules/network"
  zone1       = local.zone1
  zone2       = local.zone2
  eks_name    = local.eks_name
  environment = local.environment
}

module "eks" {
  source                     = "./modules/eks"
  eks_name                   = local.eks_name
  environment                = local.environment
  k8s_version                = local.k8s_version
  private_subnet_ids         = module.network.private_subnet_ids
  k8s_rbac_viewer_group_name = "booking-service-viewer"
  k8s_rbac_admin_group_name  = "booking-service-admin"
  region                     = local.region
  depends_on                 = [module.network]
}