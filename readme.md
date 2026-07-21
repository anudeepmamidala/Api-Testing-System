# ProbeAPI

ProbeAPI is a full-stack API testing platform (Postman-style) with a Spring Boot backend and React frontend.
It supports authenticated users, request execution through a backend proxy, request history, saved requests, and collections.

## Current Project Status

- Backend package: `ApiDashboard/src/main/java/com/anudeep/probeapi`
- Frontend app: `frontend`
- Auth: JWT-based with Spring Security
- Database (current default): H2 in-memory
- Build status: backend `mvn clean install` passes

## Main Features

- Register and login users with JWT
- Execute external HTTP APIs (GET/POST/PUT/DELETE/PATCH/HEAD/OPTIONS)
- Capture status code, response body, response headers, and latency
- Persist request history per user
- Save request templates and organize them in collections
- Basic security hardening (validation + SSRF checks)

## High-Level Architecture

`React frontend -> Spring Boot backend -> external APIs + database`

Backend follows:
`Controller -> Service -> Repository -> Database`

## API Overview

Base URL (local): `http://localhost:8080/api`

- Auth
  - `POST /auth/register`
  - `POST /auth/login`
  - `GET /auth/health`
- Request execution
  - `POST /request/execute`
- History
  - `GET /history`
  - `GET /history/{id}`
  - `DELETE /history/{id}`
  - `DELETE /history`
- Storage: saved requests
  - `POST /storage/saved-requests`
  - `GET /storage/saved-requests`
  - `GET /storage/saved-requests/{id}`
  - `PUT /storage/saved-requests/{id}`
  - `DELETE /storage/saved-requests/{id}`
- Storage: collections
  - `POST /storage/collections`
  - `GET /storage/collections`
  - `GET /storage/collections/{id}`
  - `PUT /storage/collections/{id}`
  - `DELETE /storage/collections/{id}`

## Run Locally

### 1) Backend

```bash
cd ApiDashboard
mvn clean install
mvn spring-boot:run
```

Backend starts at `http://localhost:8080/api`.

### 2) Frontend

```bash
cd frontend
npm install
npm start
```

Frontend starts at `http://localhost:3000`.

## Environment Notes

- Frontend env file: `frontend/.env`
  - `REACT_APP_API_URL=http://localhost:8080/api`
- Backend config file: `ApiDashboard/src/main/resources/application.properties`
- Replace JWT secret before deployment.

## Known Next Improvements

- Add fuller test coverage (unit/integration/e2e)
- Add production profile with PostgreSQL + migrations
- Add async request jobs and request chaining
- Add rate limiting and more observability
