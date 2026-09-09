# Pensionat Kademina — Customer Service

The **customer service** is a small Spring Boot REST API that owns guest ("customer") records for the Pensionat Kademina guesthouse system. It's consumed by the [booking service](https://github.com/gabbson42/pensionat-kademina-booking), which uses it to list, create, edit, and delete customers from its UI.

## Part of a microservices system

| Service | Repo | Responsibility |
|---|---|---|
| Booking service | [`pensionat-kademina-booking`](https://github.com/gabbson42/pensionat-kademina-booking) | Rooms, bookings, UI — calls this service |
| **Customer service** (this repo) | `pensionat-kademina-customer-service` | Customer records (this service) |
| Rating service | [`pensionat-kademina-rating-service`](https://github.com/gabbson42/pensionat-kademina-rating-service) | Guest reviews/ratings |

This service is a standalone REST API with no outbound calls to the other services — it's purely consumed by the booking service. In the shared `docker-compose.yml` (in the booking service repo) it runs as `customer-service`, backed by its own `customer_db` MySQL database, and is reachable on host port `8081` (container port `8080`).

## Tech stack

- Java 25, Spring Boot (`spring-boot-starter-parent` 4.1.1)
- Spring MVC (REST, no UI)
- Spring Data JPA + MySQL
- Bean Validation
- Lombok
- Testcontainers (MySQL) for integration tests
- Maven (wrapper included), Docker, Kubernetes manifests (`k8s/`)

## Domain model

`Customer` — `id`, `name`.

## API

Base path: `/customer`

| Method | Path | Description |
|---|---|---|
| `GET` | `/customer` | List all customers |
| `GET` | `/customer/{id}` | Get a customer by id |
| `POST` | `/customer/add` | Create a customer (request body: raw name string) |
| `POST` | `/customer/edit` | Update a customer's name (request body: `CustomerDto`) |
| `POST` | `/customer/delete/{id}` | Delete a customer by id |

`CustomerDto`:
```json
{
  "id": 1,
  "name": "Gabriel"
}
```

> Note: deleting a customer who has an active booking is rejected by the booking service, which owns that business rule.

## Getting started

### Prerequisites
- Java 25
- MySQL 8 (or run via Docker Compose from the booking service repo — see below)
- Maven (or use the included `./mvnw`)

### Run locally with Maven
```bash
./mvnw spring-boot:run
```
Default datasource configuration (override via env vars or `application.properties`):
```properties
spring.datasource.url=jdbc:mysql://customer-db:3306/customer_db
spring.datasource.username=springUser
spring.datasource.password=secretPassword
```

### Run as part of the full system
This service has no docker-compose file of its own — it's one of three services wired up by the `docker-compose.yml` in the [booking service repo](https://github.com/gabbson42/pensionat-kademina-booking). Clone all three repos as siblings and run `docker compose up --build` from there. This service will be available at **http://localhost:8081**.

### Build & run standalone
```bash
./mvnw clean package
docker build -t pensionat-kademina-customer-service .
docker run -p 8081:8080 pensionat-kademina-customer-service
```

### Kubernetes
Manifests for this service and its database are in [`k8s/`](k8s):
```bash
kubectl apply -f k8s/customer-db.yaml
kubectl apply -f k8s/customer-service.yaml
```

## Tests

```bash
./mvnw test
```
Includes integration tests backed by Testcontainers' MySQL module, so Docker must be available to run the full test suite.

## Notes
- Purely a data-owning REST service: it has no knowledge of bookings or reviews, keeping it independently deployable.
