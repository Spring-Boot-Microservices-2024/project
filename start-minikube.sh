eval $(minikube docker-env)

minikube addons enable ingress

echo "Applying Kubernetes configurations..."
kubectl apply -f k8s-configuration/redis-deployment.yml
kubectl wait --for=condition=ready pod -l app=redis --timeout=200s

kubectl apply -f k8s-configuration/analytics-deployment.yml
kubectl wait --for=condition=ready pod -l app=analytics --timeout=200s

kubectl apply -f k8s-configuration/booking-configmap.yml
kubectl apply -f k8s-configuration/booking-deployment.yml
kubectl apply -f k8s-configuration/ingress.yml

kubectl wait --for=condition=ready pod --all --timeout=200s