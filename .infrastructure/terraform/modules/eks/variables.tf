variable "eks_name" {
  type = string
  nullable = false
}

variable "environment" {
  type = string
  nullable = false
}

variable "k8s_version" {
  type = string
  nullable = false
}

variable "private_subnet_ids" {
  type = list(string)
  nullable = false
}

variable "k8s_rbac_viewer_group_name" {
  type = string
  default = "k8s-rbac-viewer"
}

variable "k8s_rbac_admin_group_name" {
  type = string
  default = "k8s-rbac-admin"
}