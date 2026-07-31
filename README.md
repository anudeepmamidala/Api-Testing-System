# ProbeAPI — API Testing & Management Platform

A secure, self-hosted API testing platform (Postman-style) built on Spring Boot, with hardened SSRF protection and per-user data isolation.

## Overview

ProbeAPI lets authenticated users build, save, and run HTTP requests, organize them into collections, and browse paginated request history — all scoped strictly to their own account.

## Key Features

- **JWT Authentication** — full auth flow (`AuthController`, `JwtService`, `JwtFilter`) securing every endpoint via Spring Security.
- **SSRF Protection** — a custom `SecurityValidator` resolves the target hostname via DNS and blocks requests to private/internal IP ranges (`10.x`, `172.16–31.x`, `192.168.x`, loopback, link-local, IPv6 equivalents) before any outbound call is made.
- **Zero-Trust Data Isolation** — every database query is scoped to the authenticated user via JWT claims across 5 relational tables (`User`, `Collection`, `HistoryEntry`, `NamedRequest`, `RequestJob`).
- **Layered Validation** — request URL format, JSON body syntax, payload size limits, and HTTP method allowlisting are all checked before a request is executed.
- **REST API** — full CRUD across a 4-layer architecture (Controller → Service → Repository → DB) for templates, collections, and history.

## Tech Stack

| Layer | Technologies |
|---|---|
| Backend | Java, Spring Boot, Spring Security |
| Auth | JWT |
| Database | PostgreSQL / MySQL |
| Validation | Custom SSRF + request validators |


## Architecture
<img width="2816" height="1536" alt="Gemini_Generated_Image_c36dvvc36dvvc36d" src="https://github.com/user-attachments/assets/78e413c6-35be-4058-b547-796e8c65c7af" />



## Project Structure
ApiDashboard/
└── src/main/java/com/anudeep/probeapi/
    ├── controller/    # ApiController, AuthController, HistoryController, StorageController
    ├── security/      # JwtFilter, JwtService, SecurityConfig
    ├── validation/     # RequestValidator, SecurityValidator (SSRF)
    ├── service/        # ApiService, AuthService, HistoryService, StorageService
    ├── repository/     # Spring Data repositories per entity
    ├── entity/         # User, Collection, HistoryEntry, NamedRequest, RequestJob
    └── dto/             # Request/response DTOs
```

## Architecture


Every request passes through JWT auth and claim extraction before reaching the Validation & Security layer, where the SSRF blocker resolves the target hostname via DNS and checks it against private/internal IP ranges. Only requests that pass both SSRF and format/payload validation reach the business logic layer and, ultimately, the external API target. All persistence is scoped to the authenticated user's ID across all 5 tables.

## License

MIT
