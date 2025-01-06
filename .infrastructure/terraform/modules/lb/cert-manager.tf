resource "helm_release" "cert_manager" {
  chart = "cert-manager"
  name  = "cert-manager"
  repository = "https://charts.jetstack.io"
  create_namespace = true
  namespace = "cert-manager"
  version = "v1.16.2"

  set {
    name = "installCRDs"
    value = "true"
  }

  depends_on = [helm_release.ingress_nginx]
}