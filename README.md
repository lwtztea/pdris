# pdris 2021
````
gradle clean build
docker build -t lwtztea/eureka_service eureka_service
docker build -t lwtztea/predict_service predict_service
docker build -t lwtztea/weather_service weather_service
docker build -t lwtztea/currency_service currency_service
docker-compose up
