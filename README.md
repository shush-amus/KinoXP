# KinoXP Case 1

KinoXP is a continuation of the earlier fullstack cinema reservation project.  
This version is rebuilt to satisfy the 3rd semester EK project requirements for case 1:

- Spring Security in the main application
- Login/logout with session-based authentication via cookies
- Authorization so users only see and manage their own reservations
- A separate microservice secured with JWT
- REST communication between the two services using Spring `RestClient`

## Architecture

The solution consists of two Spring Boot applications:

1. `KinoXp`
Main web application with:
- vanilla JavaScript frontend
- session-based login/logout
- reservation handling
- H2 database

2. `pricing-service`
Internal microservice with:
- JWT-protected endpoint
- reservation price calculation
- stateless security

Flow:

1. User logs into the main application.
2. Spring Security creates a server-side session.
3. The browser keeps the session through cookies.
4. When a reservation is created, the main application calls `pricing-service`.
5. The internal call is authenticated with a JWT.
6. Only the logged-in user's own reservations are returned from `/api/reservations`.

## Project Requirements Covered

### Security in the application
- Implemented with Spring Security in the main app
- Login endpoint: `POST /api/auth/login`
- Logout endpoint: `POST /api/auth/logout`
- Current user endpoint: `GET /api/auth/me`

### Access control
- Anonymous users can view showings
- Authenticated users can create and view reservations
- Users can only access their own reservations
- Admin-only write access is reserved for showing management endpoints

### Session-based authentication with cookies
- Frontend and backend use session authentication
- Session is stored server-side by Spring Security
- Browser sends the session cookie automatically
- CSRF token is fetched from `/api/auth/csrf` and sent on state-changing requests

### Microservice with REST + JWT
- The pricing logic is moved into `pricing-service`
- Main app calls the microservice through Spring `RestClient`
- The microservice only accepts requests with a valid Bearer JWT
- JWT contains audience `pricing-service` and scope `pricing:calculate`

## Demo Users

Use these accounts for testing:

- `test` / `test123`
- `john` / `test123`
- `admin` / `admin123`

Log in as `test` and create reservations.  
Log out and log in as `john` to show that `john` cannot see `test`'s reservations.

## Setup

### Requirements
- Java 17+
- Maven Wrapper or Maven installed locally

### Run the pricing microservice

From the repository root:

```bash
./mvnw -f pricing-service/pom.xml spring-boot:run
```

Or on Windows:

```powershell
.\mvnw.cmd -f pricing-service\pom.xml spring-boot:run
```

The service starts on `http://localhost:8081`.

### Run the main application

From the repository root:

```bash
./mvnw spring-boot:run
```

Or on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The main app starts on `http://localhost:8080`.

## Configuration

Main application properties:

- `pricing.service.base-url=http://localhost:8081`
- `pricing.service.jwt-secret=kinoxp-shared-secret-for-service-jwt-12345`

Microservice properties:

- `server.port=8081`
- `service.jwt-secret=kinoxp-shared-secret-for-service-jwt-12345`

Important: the JWT secret must match in both services.

## Main Endpoints

### Main application
- `GET /api/showings`
- `GET /api/reservations`
- `POST /api/reservations`
- `DELETE /api/reservations/{id}`
- `GET /api/auth/csrf`
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/auth/me`

### Pricing microservice
- `POST /api/internal/pricing/calculate`

## Suggested Exam Demo

This is a simple way to present the project:

1. Show that the public page can load movie showings without login.
2. Log in as `test`.
3. Create a reservation and explain that price calculation happens in the JWT-protected microservice.
4. Show `/api/reservations` only returns `test`'s own data.
5. Log out and log in as `john`.
6. Show that `john` cannot see `test`'s reservations.
7. Explain that the browser uses a session cookie, while service-to-service communication uses JWT.

## Notes

This solution is intentionally scoped to be realistic for a one-week school project:

- small enough to finish
- clear enough to explain at exam
- aligned with Spring Security, REST integration and microservice concepts from the course
