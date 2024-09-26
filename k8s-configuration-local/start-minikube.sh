eval $(minikube docker-env)

minikube addons enable ingress

cd ../project

echo "Cleaning and building spring-app..."
./gradlew clean build

echo "Building Docker image for spring-app..."
docker build -t spring-app .

cd ../analytics-service

echo "Cleaning and building spring-analytics..."
./gradlew clean build

echo "Building Docker image for spring-analytics..."
docker build -t spring-analytics .

cd ../k8s-configuration-local

echo "Applying Kubernetes configurations..."
kubectl apply -f redis-deployment.yml
kubectl wait --for=condition=ready pod -l app=redis --timeout=200s

kubectl apply -f analytics-deployment.yml
kubectl wait --for=condition=ready pod -l app=analytics --timeout=200s

kubectl apply -f booking-configmap.yml
kubectl apply -f booking-deployment.yml
kubectl apply -f ingress.yml

kubectl wait --for=condition=ready pod --all --timeout=200s