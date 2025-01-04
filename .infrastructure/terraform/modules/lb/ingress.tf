resource "helm_release" "ingress_nginx" {
  name       = "ingress_nginx"
  repository = "https://kubernetes.github.io/ingress-nginx"
  chart      = "ingress-nginx"
  namespace = "nginx"
  create_namespace = true
  version    = "4.12.0"

  values = [
    file("${path.module}/values/ingress_nginx_values.yaml}")
  ]

  depends_on = [helm_release.lbc_release]
}