### Run (from this directory)
```
minikube delete
minikube start
kubectl config use-context minikube
eval $(minikube docker-env)

gradle clean build -p ..

docker build -t lwtztea/currency_service ../currency_service
docker build -t lwtztea/weather_service ../weather_service
docker build -t lwtztea/predict_service ../predict_service
docker build -t lwtztea/eureka_service ../eureka_service

kubectl create -f postgres
kubectl create -f eureka_service
kubectl create -f currency_service
kubectl create -f weather_service
kubectl create -f predict_service
```

### Get urls for services
```
minikube service eureka --url
minikube service currency --url
minikube service weather --url
minikube service predict --url
```