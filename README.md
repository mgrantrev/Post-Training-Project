# Media Manager

A full-stack web application for managing media content, built with Spring Boot and Angular.

## Overview

Media Manager is a Chinook database-backed application that allows customers to browse their purchased tracks and employees to manage sales metrics and support tickets.

## Tech Stack

**Backend:**
- Java 21
- Spring Boot 4.x
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL (production) / H2 (testing)

**Frontend:**
- Angular 21
- TypeScript
- Playwright (E2E testing)

**Testing:**
- JUnit 5 & RestAssured (API tests)
- Selenium & Cucumber (E2E tests)
- Playwright (E2E tests)

## Project Structure

```
post-training/
└── Media Manager/
    ├── backend/          # Spring Boot application
    └── frontend/         # Angular application
```

## Getting Started

### Backend
```bash
cd "post-training/Media Manager/backend"
./gradlew bootRun
```

### Frontend
```bash
cd "post-training/Media Manager/frontend"
npm install
npm start
```

### Running Tests

**Backend Tests:**
```bash
./gradlew test           # Unit & API tests
./gradlew e2eTest        # Selenium/Cucumber tests
```

**Frontend Tests:**
```bash
npx playwright test
```

## Default Users

| Username | Password | Role |
|----------|----------|------|
| customer1 | password | Customer |
| employee1 | password | Employee |

## Features

- **Authentication:** JWT-based login with role-based access control
- **Customer Dashboard:** Browse purchased tracks with search/filter
- **Employee Dashboard:** View sales metrics and manage support tickets
- **Support System:** Create, respond to, and close support tickets
