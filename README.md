# 🎬 Cinema PSW - Full-Stack Movie Booking & Management Platform

> **Academic Project** developed for the university course **"Piattaforme Software e Applicazioni Web" (Software Platforms and Web Applications)** — Bachelor's Degree in Computer Engineering (*Laurea Triennale in Ingegneria Informatica*).

A modern, full-stack cinema reservation and ticket management system built with **Spring Boot 3**, **Angular 20**, and **Keycloak** for OAuth2/OIDC authentication. Designed for secure, concurrent booking operations and smooth user experience.

---

## 🌟 Key Features

- **Movie & Screening Catalog**: Browse current and upcoming movies, view screening times, room allocations, and filter projections by date or movie with paginated results.
- **Seat Selection & Concurrency Control**: Interactive seat picker with temporary locking mechanisms (5-minute hold) to avoid double-booking race conditions during checkout.
- **Ticket Purchase & Transactions**: Multi-ticket booking transactions, price computation, simulated payment processing, and atomic persistence.
- **Refund & Cancellation Policy**: Automated cancellations with business rules (e.g. eligible for refund if cancelled more than 60 minutes before showtime).
- **Customer Portal**: Personal dashboard tracking active tickets, order history, and profile synchronization with identity tokens.
- **Role-Based Access Control (RBAC)**: Protected endpoints and UI routing differentiating standard customers and cinema administrators via Keycloak OpenID Connect.

---

## 🛠️ Technology Stack

### Backend
- **Java 17** & **Spring Boot 3.5**
- **Spring Security (OAuth2 Resource Server)**: Stateless JWT token authentication & role mapping
- **Spring Data JPA & Hibernate**: Relational persistence layer
- **PostgreSQL**: Production relational database
- **H2 Database**: Fast in-memory database configured for automated unit and integration tests
- **Jakarta Bean Validation**: Robust request validation
- **Lombok**: Boilerplate reduction

### Frontend
- **Angular 20** (Standalone Components, Signals, Reactive Architecture)
- **TypeScript**
- **angular-oauth2-oidc**: Authorization Code Flow with PKCE
- **CSS3**: Custom responsive styling & design tokens

### Identity & Access Management (IAM)
- **Keycloak**: Centralized authentication, JWT issuing, user/role management

---

## 📁 Project Structure

```text
cinema/
├── src/
│   ├── main/
│   │   ├── java/org/example/cinema/
│   │   │   ├── configuration/     # Security, OAuth2, and Filter configurations
│   │   │   ├── controllers/       # REST API endpoints (Film, Proiezione, Biglietto, Cliente)
│   │   │   ├── dto/               # Request and response data transfer objects
│   │   │   ├── entities/          # JPA entities (Film, Proiezione, Sala, Posto, Biglietto, Cliente)
│   │   │   ├── exceptions/        # Custom exception classes and handlers
│   │   │   ├── repositories/      # Spring Data JPA repositories
│   │   │   └── services/          # Business logic and transaction management
│   │   └── resources/
│   │       ├── application.properties          # Production configuration (supports env vars)
│   │       └── application.properties.example  # Example template for local configuration
│   └── test/
│       ├── java/org/example/cinema/            # Integration and context tests
│       └── resources/application-test.properties # Isolated test configuration (H2)
├── frontend/
│   └── cinemaAngular/
│       ├── src/
│       │   ├── app/
│       │   │   ├── acquisto/              # Seat selection & purchase flow
│       │   │   ├── catalogofilm/          # Movie catalog view
│       │   │   ├── catalogoproiezioni/    # Projection listing & filters
│       │   │   ├── cliente/               # User profile & tickets dashboard
│       │   │   ├── guards/                # Route authorization guards
│       │   │   └── services/              # API & state management services
│       │   ├── index.html
│       │   └── styles.css
│       └── package.json
├── pom.xml                                # Maven build definition
├── .gitignore                             # Ignore rules for build, IDE, and secret files
└── README.md
```

---

## ⚙️ Configuration & Environment Variables

The backend uses environment variables with sane local defaults in `application.properties`. Sensitive credentials are never committed.

| Variable | Description | Default |
|---|---|---|
| `PORT` | Backend server port | `9090` |
| `DB_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://localhost:5432/cinema-db` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `root` |
| `KEYCLOAK_ISSUER_URI` | Keycloak realm issuer endpoint | `http://localhost:8080/realms/myrealm` |
| `KEYCLOAK_JWK_SET_URI` | Public keys certs endpoint | `http://localhost:8080/realms/myrealm/protocol/openid-connect/certs` |
| `KEYCLOAK_CLIENT_ID` | OAuth2 client identifier | `cinema-client` |
| `KEYCLOAK_TOKEN_URI` | OAuth2 token endpoint | `http://localhost:8080/realms/myrealm/protocol/openid-connect/token` |

An example configuration file is provided at `src/main/resources/application.properties.example`.

---

## 🚀 Getting Started

### Prerequisites
- **JDK 17+**
- **Maven 3.8+** (or use included `./mvnw`)
- **Node.js 20+** and **npm**
- **PostgreSQL** running on port 5432
- **Keycloak** running on port 8080 (Realm: `myrealm`, Client ID: `cinema-client`)

---

### 1. Database Setup
Create a PostgreSQL database named `cinema-db`:
```sql
CREATE DATABASE "cinema-db";
```

### 2. Backend Setup
1. Open the project root directory.
2. Build and run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   *The backend will start on `http://localhost:9090`.*

3. Run automated tests (runs on in-memory H2 without requiring PostgreSQL):
   ```bash
   ./mvnw test
   ```

---

### 3. Frontend Setup
1. Navigate to the Angular client folder:
   ```bash
   cd frontend/cinemaAngular
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Angular development server:
   ```bash
   npm start
   ```
   *The frontend application will be accessible at `http://localhost:4200`.*
