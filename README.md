🏨 Hotel Management Platform — Microservices Architecture

A production-grade Cloud-Native Hotel Management System built using Spring Boot Microservices, Spring Cloud Gateway, Eureka, Spring Cloud Config, JWT Authentication, and Role-Based Authorization.

🚀 System Overview

| Component             | Responsibility                                      |
| --------------------- | --------------------------------------------------- |
| **Auth Service**      | Registration, Login, JWT Token Generation           |
| **API Gateway**       | Central entrypoint, Routing, Authorization via RBAC |
| **Config Server**     | Centralized externalized configuration (Git-backed) |
| **Eureka Server**     | Service Discovery                                   |
| **Hotel Service**     | Hotels & Rooms management                           |
| **Booking Service**   | Booking creation & cancellation                     |
| **Inventory Service** | Date-wise inventory management for rooms            |
| **Loyalty Service**   | Reward points for bookings                          |
| **Payment Service**   | Payment processing & history                        |
| **User Service**      | User profile & role management                      |

🔐 Security Model

| Feature         | Implementation                        |
| --------------- | ------------------------------------- |
| Authentication  | **Auth Service** issues JWT           |
| Authorization   | **API Gateway** validates JWT & roles |
| Token Payload   | `email`, `roles`, `expiry`            |
| Token Transport | Sent as `HttpOnly` **Secure Cookie**  |


🌍 API Endpoints (Service-Wise)

Each endpoint is grouped by Access Level: 🔓 Public • 👤 User • 🛎️ Hotel-Manager • 🛡 Admin • 🔁 Internal

🔐 Auth Service

| Method | Endpoint                | Access    | Description              |
| ------ | ----------------------- | --------- | ------------------------ |
| POST   | `/api/v1/auth/register` | 🔓 Public | Register                 |
| POST   | `/api/v1/auth/login`    | 🔓 Public | Login & issue JWT cookie |

🏬 Hotel Service

| Method | Endpoint                                       | Access      | Description    |
| ------ | ---------------------------------------------- | ----------- | -------------- |
| GET    | `/api/v1/hotels`                               | 🔓 Public   | View hotels    |
| POST   | `/api/v1/hotel-manager/hotels`                 | 🛎️ Manager | Create a hotel |
| POST   | `/api/v1/hotel-manager/hotels/{hotelId}/rooms` | 🛎️ Manager | Add a room     |
| PUT    | `/api/v1/hotel-manager/hotels/{hotelId}`       | 🛎️ Manager | Update hotel   |
| PUT    | `/api/v1/hotel-manager/hotels/rooms/{roomId}`  | 🛎️ Manager | Update room    |
| DELETE | `/api/v1/admin/hotels/{hotelId}`               | 🛡 Admin    | Delete hotel   |
| DELETE | `/api/v1/admin/hotels/rooms/{roomId}`          | 🛡 Admin    | Delete room    |

📅 Booking Service

| Method | Endpoint                              | Access  | Description    |
| ------ | ------------------------------------- | ------- | -------------- |
| POST   | `/api/v1/bookings`                    | 👤 User | Create booking |
| POST   | `/api/v1/bookings/{bookingId}/cancel` | 👤 User | Cancel booking |


📦 Inventory Service

| Method | Endpoint                                              | Access      | Description             |
| ------ | ----------------------------------------------------- | ----------- | ----------------------- |
| GET    | `/api/v1/inventory/check?roomId=&startDate=&endDate=` | 🔓 Public   | Check availability      |
| POST   | `/api/v1/hotel-manager/inventory/init?...`            | 🛎️ Manager | Setup initial inventory |
| PUT    | `/api/v1/hotel-manager/inventory/mark/booked`         | 🔁 Internal | Block dates             |
| PUT    | `/api/v1/hotel-manager/inventory/mark/available`      | 🔁 Internal | Release dates           |


⭐ Loyalty Service

| Method | Endpoint            | Access      | Description     |
| ------ | ------------------- | ----------- | --------------- |
| POST   | `/loyalty/add`      | 🔁 Internal | Add points      |
| POST   | `/loyalty/redeem`   | 🔁 Internal | Redeem points   |
| GET    | `/loyalty/{userId}` | 👤 User     | Get user points |


💳 Payment Service

| Method | Endpoint                  | Access  | Description     |
| ------ | ------------------------- | ------- | --------------- |
| POST   | `/payments/process`       | 👤 User | Make payment    |
| GET    | `/payments/user/{userId}` | 👤 User | Payment history |


👤 User Service

| Method | Endpoint                                  | Access      | Description                 |
| ------ | ----------------------------------------- | ----------- | --------------------------- |
| GET    | `/api/v1/users/email/{email}`             | 🔁 Internal | Get by email                |
| GET    | `/api/v1/users/{userId}`                  | 🔁 Internal | Get by ID                   |
| PUT    | `/api/v1/users/{userId}/update`           | 👤 User     | Update profile              |
| PUT    | `/api/v1/users/{userId}/update`           | 🛡 Admin    | Promote to hotel manager    |
| GET    | `/api/v1/admin/users`                     | 🛡 Admin    | Users waiting for promotion |
| DELETE | `/api/v1/admin/users/{userId}/deactivate` | 🛡 Admin    | Deactivate user             |


🌐 System Interaction & Flow

- Config Server holds centralized configurations (YAML/properties) in a Git repo. Each microservice reads its config from Config Server at startup.

- Eureka performs service registration and discovery; gateway & services use it for load-balancing and service resolution.

- API Gateway validates JWT and enforces role-based authorization (User / Hotel-Manager / Admin). After validation, it forwards user identity (email + roles) to downstream services via headers.

- Booking orchestrates multi-service flows: checks inventory → reserves → processes payment → confirms booking → notifies loyalty.

- Circuit Breaker (Resilience4j) is applied in orchestrator (Booking Service) to prevent cascading failures.


⚙️ Tech Stack

| Area              | Technology                                     |
| ----------------- | ---------------------------------------------- |
| Language          | Java 17                                        |
| Framework         | Spring Boot 3                                  |
| Service Discovery | Eureka                                         |
| Config Management | Spring Cloud Config (Git-backed)               |
| Gateway           | Spring Cloud Gateway                           |
| Security          | Spring Security + JWT                          |
| DB per Service    | MySQL                                          |
| Build             | Maven                                          |
| Containerization  | Docker                                         |
| Deployment        | Railway (microservices deployed as containers) |


✅ Key Highlights

- Centralized config via Config Server (Git-backed), enabling safe, environment-specific configs and runtime refresh.

- API Gateway provides a single entry point and offloads authorization logic.

- Each microservice owns its data (no shared DB) & is independently deployable.

- HttpOnly Secure Cookies for JWT (mitigates XSS).

- Resilience: Circuit Breaker for booking orchestration.

- Good for interviews: deployed, documented, real-world flows, and trade-offs explained.


🔧 Local Development & Testing

Environment variables (examples):

- EUREKA_CLIENT_SERVICEURL_DEFAULTZONE → Eureka URL

- SPRING_CLOUD_CONFIG_URI → Config Server URL

- SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD → DB credentials

Run locally (example):

1. Start Config Server (it points to your Git repo with service configs)

2. Start Eureka Server

3. Start services (Auth, User, Hotel, Inventory, Booking, Payment, Loyalty)

4. Start API Gateway last

🔁 CI/CD & Deployment Notes

- Each microservice contains a Dockerfile for container builds.

- Deployment target: Railway — services are deployed as individual containers with environment variables set via Railway UI.

- Config Server reads from a Git repo so updating central config is just a commit & refresh.

🧩 Future Enhancements (Roadmap)

| Area               | Current Status | Needed for Enterprise   |
| ------------------ | -------------- | ----------------------- |
| Auth & Gateway     | ✅              | Optional OAuth2         |
| Config & Discovery | ✅              | Stable                  |
| Logging            | ⚠️ Basic       | Grafana Loki            |
| Monitoring         | ❌              | Micrometer + Prometheus |
| Tracing            | ❌              | Sleuth + Zipkin         |
| API Docs           | ❌              | Swagger/OpenAPI         |
| DB Migrations      | ⚠️ Manual      | Flyway                  |
| Caching            | ❌              | Redis                   |
| Messaging          | ❌              | Kafka (optional)        |
| Tests              | ⚠️ Minimal     | Unit + Integration      |
| CI/CD              | ⚠️ Manual      | GitHub Actions          |
| Observability      | ⚠️ Partial     | Actuator endpoints      |
