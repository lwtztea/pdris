# pdris 2021
````
gradle clean build
docker build -t eureka_service eureka_service
docker build -t predict_service predict_service
docker build -t weather_service weather_service
docker build -t currency_service currency_service
docker-compose up
