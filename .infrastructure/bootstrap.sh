kubectl apply -k "github.com/kubernetes-sigs/aws-ebs-csi-driver/deploy/kubernetes/overlays/stable/?ref=master"
helm upgrade bs-release ./booking-service --install --set global.ecr.password=$(aws ecr get-login-password) -n default
