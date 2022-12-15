This project contains 4 modules, and is meant as a sample to demonstrate different ways of communicating between microservices.
The 4 modules are:
* api
* api-service
* logistics-service
* sales-service

## API
This module contains the API spec. OpenAPI is used to generate the interfaces and model of the API.

## API-service
This module depends on the `API` module and implements the API. The API-implementation integrates with the 2 'internal' services (sales and logistics).
This service also subscribes to the `orders` topic, where `ConfirmedOrders` are published.

## logistics-service
This service is called by the API-service, and contains logistics-specific functionalities.

## sales-service
This service is called by the API-service and contains sales-specific functionalities.
The service integrates with a kafka-cluster and publishes `ConfirmedOrders` on the `orders` topic.


# Building

`mvn clean install`

# Running

To start the local kafka cluster:
`docker-compose up`

All 3 service can be started by means of the same command:
```
mvn -f ./api-service/pom.xml spring-boot:run
mvn -f ./logistics-service/pom.xml spring-boot:run
mvn -f ./sales-service/pom.xml spring-boot:run
```

# Using

Only the `API-service` is supposed to be called directly.

Request a list of products:
```
curl http://localhost:8080/furniture-api/products
```
The `api-service` will forward the request to the `logistics-service`.
Implementation: `be.reaktika.vives.api.web.controller.ProductController`

Place an order:
```
curl -X POST -H 'Content-type: application/json' -d '[{"productId":100, "quantity":5}]' http://localhost:8080/furniture-api/order
```
The order-request will be forwarded to the `sales-service` (`be.reaktika.vives.api.web.controller.OrderApiController`), where it will processed.
The result of that processing will be published to the `orders` topic (`be.reaktika.vives.service.sales.OrderService`).
The `be.reaktika.vives.api.web.controller.OrderController` will produce a response when the appropriate `ConfirmetOrder` is consumed from the topic (`be.reaktika.vives.api.OrderConsumerService`).

