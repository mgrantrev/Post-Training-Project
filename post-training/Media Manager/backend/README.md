# Media Manager - Backend

Spring Boot backend scaffold for Media Manager project.

Features included:
- Maven build
- Spring Boot 3
- Spring Data JPA (Postgres)
- Flyway migrations (initial migrations to create `app_user` and `support_ticket` and seed sample users)
- Basic JWT auth utilities and filter
- `AuthController` for login
- `SupportTicketController` for customer/employee ticket flows

To run locally:
- Ensure a Postgres instance is available
- Set environment variables or edit `application.yml`:
  - `SPRING_DATASOURCE_URL` (jdbc url), `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
  - `JWT_SECRET` (min 32 bytes recommended)
- Build and run (Gradle): `./gradlew bootJar` then `java -jar build/libs/media-manager-0.1.0.jar`, or run `./gradlew bootRun` for development.
- Docker: `docker build -t media-manager-backend:latest ./` and `docker run -e SPRING_DATASOURCE_URL="jdbc:postgresql://..." -e JWT_SECRET="your-secret" -p 8080:8080 media-manager-backend:latest`

Notes:
- Password seed values in the migration are placeholders; use BCrypt to generate hashes before seeding.
- This scaffold is a starting point; expand entities/services/controllers for the full MVP.
