# StandardService

A production-grade, enterprise-ready Spring Boot 3 microservice designed with scalability, resilience, and observability at its core.

## 🚀 Key Features

- **Spring Boot 3.5.7**: Leveraging the latest features of the Spring ecosystem.
- **Resilience4j**: Built-in fault tolerance with Circuit Breakers, Retries, and Rate Limiters.
- **Observability**: 
  - **Tracing**: Distributed tracing via OpenTelemetry and MDC integration.
  - **Logging**: Enterprise-grade logging with Log4j2 and standardized rotation.
  - **Metrics**: Prometheus metrics export via Micrometer.
- **Security**: OAuth2 Resource Server and Client integration (JWT-based).
- **Performance**: 
  - Dual-layer caching with **Redis** and **Hazelcast**.
  - Optimized async processing with dedicated thread pools.
- **API Documentation**: Automated Swagger/OpenAPI 3 documentation.
- **Audit System**: Automated JPA auditing (createdAt, updatedAt, createdBy, updatedBy) using a standardized `BaseEntity` and `BaseDTO`.

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.5.x** (Web, Data JPA, Security, Actuator, AOP)
- **Database**: MySQL (Production), H2 (Development/Testing)
- **Cache**: Redis, Hazelcast
- **Documentation**: SpringDoc OpenAPI
- **Utilities**: Lombok, MapStruct (for future mapping)

## 🏗️ Getting Started

### Prerequisites

- JDK 17+
- Maven 3.8+
- Docker (optional, for Redis/MySQL)

### Local Development (H2)

By default, the application uses the `dev` profile with an H2 in-memory database.

```bash
mvn clean install
mvn spring-boot:run
```

### Production/MySQL Setup

Update the `application-mysql.yml` or environment variables with your MySQL credentials, then run:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## 📍 Important Endpoints

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Actuator Health**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Prometheus Metrics**: [http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)
- **H2 Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (Profile: `dev`, JDBC: `jdbc:h2:mem:standard_db`)

## 🏛️ Architecture & Standards

- **Standardized DTOs**: All API interactions use the `ApiResponse<T>` wrapper.
- **Common Audit**: Entities and DTOs extend `BaseEntity` and `BaseDTO` respectively to maintain a consistent audit trail.
- **Error Handling**: Centralized `GlobalExceptionHandler` using localized `messages.properties`.

## 📝 License

Internal Enterprise Project - All Rights Reserved.
