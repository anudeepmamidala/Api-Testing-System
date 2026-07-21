# ProbeAPI Explanation + Interview Q&A

This document is for your personal explanation prep and interviews.

## Project Explanation (Short)

ProbeAPI is a backend-first API testing platform.
The frontend sends request definitions to a Spring Boot backend. The backend executes outbound HTTP calls, captures response data (status/body/headers/latency), and stores user-scoped history and saved requests.

Core value:
- avoids browser CORS limitations by using server-side execution
- keeps user data isolated with JWT authentication
- provides reusable request storage and collections

## Module-by-Module Explanation

- `controller`: exposes endpoints and delegates to services
- `service`: business logic (auth, request execution, history/storage)
- `repository`: JPA data access with user-scoped queries
- `security`: JWT generation/validation/filter
- `validation`: request checks and SSRF protection
- `dto`: API contracts
- `entity`: persistence models

## Request Execution Flow

1. User logs in and gets JWT.
2. Frontend calls `POST /api/request/execute`.
3. JWT filter authenticates and sets user context.
4. Service validates method/url/body + security rules.
5. Service executes outbound HTTP request.
6. History is stored with response metadata.
7. Response is returned to frontend.

## Interview Questions and Answers

### What problem does this project solve?
It provides a self-hosted API testing platform that avoids browser CORS limitations and centralizes request execution, validation, and persistence.

### Why Spring Boot?
It provides strong REST support, security integration, JPA tooling, and production-ready conventions.

### Why JWT instead of sessions?
JWT keeps the API stateless and easier to scale horizontally.

### How is authentication implemented?
Register/login endpoints create users and issue JWT. A filter validates tokens for protected routes and injects authenticated user context.

### How is authorization enforced?
With authenticated user context and user-scoped repository methods (e.g., `findByIdAndUser`).

### How is SSRF handled?
By validating and blocking unsafe/internal target addresses before outbound requests.

### Why DTOs?
To decouple API contracts from internal entity structures and avoid over-exposing persistence fields.

### What gets stored in history?
Method, URL, request payload/headers, response status/body/headers, latency, and timestamps per user.

### How are errors handled?
A global exception handler returns structured error payloads for validation, business, and unexpected exceptions.

### What are current limitations?
Execution is synchronous; production hardening (migrations, deeper tests, rate limiting) can be expanded.

### What would you improve next?
Async jobs, request chaining, stronger testing coverage, PostgreSQL production profile, and observability.

## 60-Second Interview Pitch

"I built ProbeAPI as a backend-first API testing platform using Spring Boot and React. The backend executes HTTP requests server-side to avoid CORS issues, captures full response metadata, and persists user-scoped history and saved requests. Authentication is JWT-based, authorization is enforced via user-scoped queries, and validation includes SSRF protection. The project follows clean controller-service-repository layering and is structured for production hardening with features like async execution and request chaining as next steps."
# ProbeAPI Explanation + Interview Q&A

This document is for your personal explanation prep and interviews.

## Project Explanation (Short)

ProbeAPI is a backend-first API testing platform.
The frontend sends request definitions to a Spring Boot backend. The backend executes outbound HTTP calls, captures response data (status/body/headers/latency), and stores user-scoped history and saved requests.

Core value:
- avoids browser CORS limitations by using server-side execution
- keeps user data isolated with JWT authentication
- provides reusable request storage and collections

## Module-by-Module Explanation

- `controller`: exposes endpoints and delegates to services
- `service`: business logic (auth, request execution, history/storage)
- `repository`: JPA data access with user-scoped queries
- `security`: JWT generation/validation/filter
- `validation`: request checks and SSRF protection
- `dto`: API contracts
- `entity`: persistence models

## Request Execution Flow

1. User logs in and gets JWT.
2. Frontend calls `POST /api/request/execute`.
3. JWT filter authenticates and sets user context.
4. Service validates method/url/body + security rules.
5. Service executes outbound HTTP request.
6. History is stored with response metadata.
7. Response is returned to frontend.

## Interview Questions and Answers

### What problem does this project solve?
It provides a self-hosted API testing platform that avoids browser CORS limitations and centralizes request execution, validation, and persistence.

### Why Spring Boot?
It provides strong REST support, security integration, JPA tooling, and production-ready conventions.

### Why JWT instead of sessions?
JWT keeps the API stateless and easier to scale horizontally.

### How is authentication implemented?
Register/login endpoints create users and issue JWT. A filter validates tokens for protected routes and injects authenticated user context.

### How is authorization enforced?
With authenticated user context and user-scoped repository methods (e.g., `findByIdAndUser`).

### How is SSRF handled?
By validating and blocking unsafe/internal target addresses before outbound requests.

### Why DTOs?
To decouple API contracts from internal entity structures and avoid over-exposing persistence fields.

### What gets stored in history?
Method, URL, request payload/headers, response status/body/headers, latency, and timestamps per user.

### How are errors handled?
A global exception handler returns structured error payloads for validation, business, and unexpected exceptions.

### What are current limitations?
Execution is synchronous; production hardening (migrations, deeper tests, rate limiting) can be expanded.

### What would you improve next?
Async jobs, request chaining, stronger testing coverage, PostgreSQL production profile, and observability.

## 60-Second Interview Pitch

"I built ProbeAPI as a backend-first API testing platform using Spring Boot and React. The backend executes HTTP requests server-side to avoid CORS issues, captures full response metadata, and persists user-scoped history and saved requests. Authentication is JWT-based, authorization is enforced via user-scoped queries, and validation includes SSRF protection. The project follows clean controller-service-repository layering and is structured for production hardening with features like async execution and request chaining as next steps."
# ProbeAPI Explanation + Interview Q&A

This document is for your personal explanation prep and interviews.

## Project Explanation (Short)

ProbeAPI is a backend-first API testing platform.  
The frontend sends request definitions to a Spring Boot backend. The backend executes outbound HTTP calls, captures response data (status/body/headers/latency), and stores user-scoped history and saved requests.

Core value:
- avoids browser CORS limitations by using server-side execution
- keeps user data isolated with JWT authentication
- provides reusable request storage and collections

## Module-by-Module Explanation

- `controller`
  - Exposes REST endpoints and delegates to services
  - Keeps controller logic thin
- `service`
  - Holds business logic (auth, request execution, history/storage operations)
- `repository`
  - JPA data access for user-scoped queries
- `security`
  - JWT token generation, validation, and request filter integration
- `validation`
  - Request validation + SSRF protection checks
- `dto`
  - API request/response contracts decoupled from entities
- `entity`
  - Persistence models for users, history, saved requests, collections, jobs

## Request Execution Flow

1. User logs in and receives JWT.
2. Frontend sends `POST /api/request/execute` with JWT.
3. JWT filter authenticates request and injects user context.
4. Service validates method/url/body and security rules.
5. Service calls external API using `RestTemplate`.
6. Response metadata is captured and stored in history.
7. Response is returned to frontend.

## Key Design Decisions

- Backend proxy model for reliability across CORS-restricted APIs
- Stateless JWT auth for scalability
- Layered architecture for maintainability
- User-scoped repository queries for authorization safety
- DTO-based responses for stable contracts

## Interview Questions and Answers

### 1) What problem does this project solve?
It gives developers a self-hosted API testing tool where request execution happens server-side, avoiding CORS issues and enabling centralized logging, validation, and persistence.

### 2) Why use Spring Boot for this project?
Spring Boot provides fast REST setup, mature security integration, JPA support, and strong ecosystem tools for production backend APIs.

### 3) Why use JWT instead of sessions?
JWT keeps the API stateless, scales better horizontally, and avoids server-side session storage.

### 4) How is authentication implemented?
`/auth/register` creates users with bcrypt-hashed passwords. `/auth/login` validates credentials and returns JWT. A JWT filter validates tokens for protected routes and attaches user context.

### 5) How do you enforce authorization?
Authorization is enforced using user-scoped queries (example: `findByIdAndUser`) and authenticated user context from JWT.

### 6) How do you prevent SSRF?
Validation blocks unsafe/internal targets such as localhost/private ranges and performs URL checks before outbound call execution.

### 7) Why separate DTOs from entities?
DTOs keep API contracts stable, hide internal fields, and reduce coupling between storage schema and external responses.

### 8) What is stored in request history?
Method, URL, request payload/headers, response status/body/headers, latency, timestamp, and user ownership reference.

### 9) How is error handling done?
A global exception handler returns structured responses for validation, custom business errors, and unexpected failures.

### 10) What trade-offs exist in the current architecture?
Synchronous outbound execution is simple but blocking; async jobs can improve scalability for heavy workloads.

### 11) How would you make this production-ready?
Use PostgreSQL profile, add migration tooling (Flyway/Liquibase), add rate limiting, stronger observability, expanded tests, and secure env-driven config.

### 12) How do you test this system?
Unit tests for services/validators, integration tests for secured endpoints and data isolation, and frontend smoke flows for login/execute/history/storage.

### 13) Why is backend proxy better than direct frontend API calls?
It bypasses browser CORS restrictions, centralizes controls/security, and provides uniform logging and persistence.

### 14) How do you secure secrets?
Use environment variables and deployment secret stores; never hardcode production JWT keys or DB credentials.

### 15) What features would you add next?
Async job execution, request chaining, assertions/test suites, environment variable sets, export/import, and team collaboration.

## Quick 60-Second Interview Pitch

"I built ProbeAPI as a backend-first API testing platform using Spring Boot and React.  
The system authenticates users with JWT, executes HTTP requests server-side to avoid CORS issues, logs complete response metadata with latency, and lets users manage saved requests and collections.  
I structured it with controller-service-repository layering, added validation and SSRF protection, and designed user-scoped data access for security.  
It is a strong base for production hardening and advanced features like async jobs and request chaining."
# ProbeAPI - Complete Module Flow & Architecture Explanation

**Date:** April 15, 2026  
**Version:** 1.0  
**Status:** Phases 1-2 Complete

---

## 📋 Table of Contents

1. [High-Level Architecture](#high-level-architecture)
2. [Module Structure](#module-structure)
3. [Authentication Flow (JWT)](#authentication-flow)
4. [Request Execution Flow](#request-execution-flow)
5. [Data Persistence Flow](#data-persistence-flow)
6. [Security & Validation Flow](#security--validation-flow)
7. [Module Interactions](#module-interactions)
8. [Database Schema](#database-schema)

---

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          CLIENT (React/Postman)                         │
│                                                                         │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐    │
│  │ Register/Login   │  │  Execute Request │  │  View History    │    │
│  └────────┬─────────┘  └────────┬─────────┘  └────────┬─────────┘    │
└───────────┼──────────────────────┼──────────────────────┼──────────────┘
            │ HTTP                 │ HTTP                 │ HTTP
            │ POST /auth/register  │ POST /request/execute│ GET /history
            │ POST /auth/login     │                      │ GET /history/{id}
            ▼                      ▼                      ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    Spring Boot REST API Layer                           │
│                       (Port 8080/api)                                   │
│                                                                         │
│  ┌──────────────────────────────────────────────────────────────────┐ │
│  │                     Request Processing Pipeline                  │ │
│  │                                                                  │ │
│  │  1. HTTP Request → 2. JWT Filter → 3. Authorization Check      │ │
│  │  4. Controller    → 5. Service   → 6. Validation Layer         │ │
│  │  7. Database      → 8. Response  → 9. HTTP Response            │ │
│  │                                                                  │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐     │ │
│  │  │ Auth Layer   │  │  Security    │  │   Exception      │     │ │
│  │  │              │  │  Validators  │  │   Handlers       │     │ │
│  │  │ - JwtService │  │              │  │                  │     │ │
│  │  │ - JwtFilter  │  │ - URL Valid  │  │ - Global Error   │     │ │
│  │  │ - SecurityCfg│  │ - SSRF Check │  │   Handling       │     │ │
│  │  └──────────────┘  │ - JSON Valid │  └──────────────────┘     │ │
│  │                    └──────────────┘                             │ │
│  └──────────────────────────────────────────────────────────────────┘ │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
            │                      │                      │
            ▼                      ▼                      ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                        Service Layer                                    │
│                                                                         │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐    │
│  │  AuthService     │  │   ApiService     │  │ StorageService   │    │
│  │                  │  │                  │  │                  │    │
│  │ - register()     │  │ - executeRequest │  │ - saveRequest()  │    │
│  │ - login()        │  │ - captureResponse│  │ - getRequests()  │    │
│  │ - tokenGenerate  │  │ - trackLatency   │  │ - Collection ops │    │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘    │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
            │                      │                      │
            ▼                      ▼                      ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    Repository Layer (JPA/Hibernate)                     │
│                                                                         │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐    │
│  │ UserRepository   │  │HistoryRepository │  │NamedRequestRepo  │    │
│  │ - save           │  │ - save           │  │ - save           │    │
│  │ - findByUsername │  │ - findByUser     │  │ - findByUser     │    │
│  │ - deleteByUser   │  │ - deleteByUser   │  │ - deleteByIdUser │    │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘    │
│                                                                         │
│  ┌──────────────────┐  ┌──────────────────┐                           │
│  │CollectionRepository │  RequestJobRepository                         │
│  │ - save           │  │ - save (Phase 4) │                           │
│  │ - findByUser     │  │ - findByIdAndUser│                           │
│  │ - delete         │  │ - findByUser     │                           │
│  └──────────────────┘  └──────────────────┘                           │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
            │                      │                      │
            ▼                      ▼                      ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      PostgreSQL Database                                │
│                                                                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌─────────┐  │
│  │ users        │  │history_entries   │named_requests   │collections   │
│  │ (5 columns)  │  │ (14 columns) │  │ (8 columns)  │  │(4 cols) │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  └─────────┘  │
│                                                                         │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │                    request_jobs (Reserved - Phase 4)             │  │
│  │                    (14 columns + JobStatus enum)                 │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## Module Structure

### 📁 Directory Organization

```
src/main/java/com/anudeep/probeapi/
│
├── ProbeApiApplication.java          ← Main entry point
│
├── config/
│   └── AppConfig.java                ← RestTemplate bean configuration
│
├── entity/                           ← JPA Entity Classes (Database Models)
│   ├── User.java
│   ├── HistoryEntry.java
│   ├── NamedRequest.java
│   ├── Collection.java
│   └── RequestJob.java
│
├── repository/                       ← Data Access Layer (JPA Repositories)
│   ├── UserRepository.java
│   ├── HistoryRepository.java
│   ├── NamedRequestRepository.java
│   ├── CollectionRepository.java
│   └── RequestJobRepository.java
│
├── dto/                              ← Data Transfer Objects (API Models)
│   ├── AuthRequestDTO.java
│   ├── AuthResponseDTO.java
│   ├── ApiRequestDTO.java
│   ├── ApiResponseDTO.java
│   ├── HistoryEntryDTO.java
│   ├── NamedRequestDTO.java
│   ├── CollectionDTO.java
│   └── JobResponseDTO.java
│
├── service/                          ← Business Logic Layer
│   ├── AuthService.java              ← Authentication logic
│   ├── ApiService.java               ← HTTP request execution
│   └── StorageService.java           ← Data persistence operations
│
├── controller/                       ← HTTP REST Endpoints
│   ├── AuthController.java           ← Auth endpoints
│   ├── ApiController.java            ← Request execution endpoints
│   ├── StorageController.java        ← Storage endpoints
│   └── HistoryController.java        ← History endpoints
│
├── security/                         ← Security Components
│   ├── JwtService.java               ← JWT token generation/validation
│   ├── JwtFilter.java                ← JWT extraction & validation filter
│   └── SecurityConfig.java           ← Spring Security configuration
│
├── validation/                       ← Input Validation
│   ├── RequestValidator.java         ← URL, JSON, HTTP method validation
│   └── SecurityValidator.java        ← SSRF protection
│
└── exception/                        ← Error Handling
    ├── CustomException.java          ← Custom exception class
    └── GlobalExceptionHandler.java   ← Global exception handler
```

---

## Authentication Flow (JWT)

### 🔐 Registration Flow

```
┌─────────────────┐
│   User Input    │
│ (username, email,
│   password)     │
└────────┬────────┘
         │ HTTP POST /auth/register
         ▼
┌──────────────────────────────────────────────────────────┐
│          AuthController.register()                       │
│  Receives: AuthRequestDTO                               │
└────────┬─────────────────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────────────────┐
│        AuthService.register()                            │
│                                                          │
│  Step 1: Validate Input                                 │
│  ├─ Check username not empty                            │
│  ├─ Check email not empty                               │
│  ├─ Check password not empty                            │
│  │                                                      │
│  Step 2: Check User Doesn't Exist                       │
│  ├─ userRepository.existsByUsername()                   │
│  ├─ userRepository.existsByEmail()                      │
│  │  └─ If exists → throw CustomException 409            │
│  │                                                      │
│  Step 3: Hash Password                                  │
│  ├─ passwordEncoder.encode(password)  ← BCrypt algo    │
│  │                                                      │
│  Step 4: Create User Entity                             │
│  ├─ User.builder()                                      │
│  │  ├─ username = input                                 │
│  │  ├─ email = input                                    │
│  │  ├─ passwordHash = hashed_password                   │
│  │                                                      │
│  Step 5: Save to Database                               │
│  ├─ userRepository.save(user)                           │
│  │  └─ DB: INSERT INTO users (...)                      │
│  │                                                      │
│  Step 6: Generate JWT Token                             │
│  ├─ jwtService.generateToken(user)                      │
│  │  └─ See JWT Generation Process below                 │
│  │                                                      │
│  Step 7: Return Response                                │
│  └─ AuthResponseDTO with token + user info              │
│                                                          │
└────────┬─────────────────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────────────────┐
│            HTTP Response 201 Created                     │
│                                                          │
│  {                                                      │
│    "userId": 1,                                         │
│    "username": "john_doe",                              │
│    "email": "john@example.com",                         │
│    "token": "eyJhbGciOiJIUzUxMiJ9...",                │
│    "message": "User registered successfully"            │
│  }                                                      │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

### 🔑 JWT Generation Process

```
┌─────────────────────────────────────────────────────────┐
│  jwtService.generateToken(user)                         │
│                                                         │
│  Input: User entity with id, username, email           │
│  Output: JWT Token string                              │
│                                                         │
│  Step 1: Create Claims Map                             │
│  ├─ claims.put("userId", user.getId())                 │
│  ├─ claims.put("username", user.getUsername())         │
│  └─ claims.put("email", user.getEmail())               │
│                                                         │
│  Step 2: Create Token                                  │
│  └─ createToken(claims, user.getUsername())            │
│     │                                                  │
│     ├─ Date now = new Date()                           │
│     ├─ Date expiry = now + 24 hours (86400000 ms)     │
│     │                                                  │
│     └─ Jwts.builder()                                  │
│        ├─ .claims(claims)          ← Add custom claims │
│        ├─ .subject(username)       ← Subject: username │
│        ├─ .issuedAt(now)           ← Issued timestamp  │
│        ├─ .expiration(expiry)      ← Expiration time   │
│        ├─ .signWith(signingKey)    ← Sign with secret  │
│        └─ .compact()               ← Encode to JWT     │
│                                                         │
│  Result:                                               │
│  eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.                │
│  eyJzdWIiOiJqb2huIiwiZW1haWwiOiJqb2huQGV4YW       │
│  ... (token string ~300-500 chars)                     │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### 🔓 Login Flow

```
┌──────────────────────────┐
│   User Credentials       │
│  (username, password)    │
└────────┬─────────────────┘
         │ HTTP POST /auth/login
         ▼
┌──────────────────────────────────────────────┐
│  AuthController.login()                      │
│  Receives: AuthRequestDTO                    │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│  AuthService.login()                         │
│                                              │
│  Step 1: Validate Input                      │
│  ├─ Check username not empty                 │
│  └─ Check password not empty                 │
│                                              │
│  Step 2: Find User by Username               │
│  ├─ userRepository.findByUsername(username)  │
│  └─ If not found → throw 401 UNAUTHORIZED    │
│                                              │
│  Step 3: Verify Password                     │
│  ├─ passwordEncoder.matches(input, hashed)   │
│  │  (BCrypt comparison - timing-safe)        │
│  └─ If no match → throw 401 UNAUTHORIZED     │
│                                              │
│  Step 4: Generate JWT Token                  │
│  ├─ jwtService.generateToken(user)           │
│                                              │
│  Step 5: Return Response                     │
│  └─ AuthResponseDTO with token               │
│                                              │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│   HTTP Response 200 OK                       │
│   (Same format as registration)              │
│   {                                          │
│     "userId": 1,                             │
│     "username": "john_doe",                  │
│     "token": "eyJhbGciOiJIUzUxMiJ9...",    │
│     "message": "Login successful"            │
│   }                                          │
└──────────────────────────────────────────────┘
```

### 🛡️ JWT Validation Flow

```
┌─────────────────────────────────────────────────────────┐
│        Incoming HTTP Request with JWT Token             │
│                                                         │
│  Header: Authorization: Bearer eyJhbGciOiJIUzUxMiJ9... │
│                                                         │
└────────┬────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────┐
│      JwtFilter.doFilterInternal()                       │
│      (Intercepts EVERY request)                         │
│                                                         │
│  Step 1: Extract Authorization Header                  │
│  ├─ request.getHeader("Authorization")                 │
│                                                         │
│  Step 2: Check Header Exists & Format                  │
│  ├─ if (header != null && header.startsWith("Bearer "))│
│  │  └─ Extract token (remove "Bearer " prefix)         │
│  │                                                     │
│  Step 3: Validate Token                                │
│  ├─ jwtService.isTokenValid(token)                     │
│  │  │                                                  │
│  │  ├─ Jwts.parser()                                   │
│  │  │  ├─ .verifyWith(signingKey)                      │
│  │  │  ├─ .build()                                     │
│  │  │  └─ .parseSignedClaims(token)                    │
│  │  │                                                  │
│  │  └─ If success → token is valid                     │
│  │     If exception → token is invalid                 │
│  │                                                     │
│  Step 4: Extract Claims                                │
│  ├─ String username = jwtService.getUsernameFromToken()│
│  ├─ Long userId = jwtService.getUserIdFromToken()      │
│  │                                                     │
│  Step 5: Set SecurityContext                           │
│  ├─ Create UsernamePasswordAuthenticationToken         │
│  ├─ SecurityContextHolder.getContext().setAuthentication(token) │
│  ├─ request.setAttribute("userId", userId)            │
│  ├─ request.setAttribute("username", username)        │
│  │                                                     │
│  Step 6: Pass to Next Filter                           │
│  └─ filterChain.doFilter(request, response)            │
│                                                         │
└────────┬────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────┐
│      SecurityConfig Routes                              │
│                                                         │
│  ✅ Public Routes (no auth required):                   │
│  ├─ /api/auth/**                                       │
│  └─ /api/health                                        │
│                                                         │
│  🔒 Protected Routes (auth required):                   │
│  ├─ /api/request/**                                    │
│  ├─ /api/history/**                                    │
│  ├─ /api/storage/**                                    │
│  └─ All other /api/** routes                           │
│                                                         │
└────────┬────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────┐
│  Controller Receives Request                            │
│  with user context populated                           │
│                                                         │
│  @RequestAttribute("userId") Long userId  ← Injected   │
│  User user = userRepository.findById(userId)           │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## Request Execution Flow

### 📡 Complete API Request Execution

```
┌──────────────────────────────────────────────────────────┐
│  Client Sends HTTP Request                              │
│                                                          │
│  POST /api/request/execute                              │
│  Authorization: Bearer <jwt_token>                      │
│  Content-Type: application/json                         │
│                                                          │
│  {                                                      │
│    "method": "GET",                                     │
│    "url": "https://api.example.com/users",              │
│    "headers": {"Accept": "application/json"},           │
│    "body": null                                         │
│  }                                                      │
│                                                          │
└──────────────┬────────────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────────┐
│  JwtFilter Validates Token                              │
│  ✅ Token valid → Continue                              │
│  ❌ Token invalid → Return 401 → End                    │
└──────────────┬────────────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────────┐
│  ApiController.executeRequest()                         │
│                                                          │
│  Receives:                                              │
│  - ApiRequestDTO (from JSON body)                       │
│  - userId (from JWT @RequestAttribute)                  │
│                                                          │
│  Step 1: Get User from Database                         │
│  └─ userRepository.findById(userId)                     │
│     (Verify user exists)                                │
│                                                          │
│  Step 2: Call Service                                   │
│  └─ apiService.executeRequest(request, user)            │
│                                                          │
└──────────────┬────────────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────────┐
│  ApiService.executeRequest()                            │
│                                                          │
│  PHASE 1: VALIDATION                                    │
│  ─────────────────────                                  │
│  ├─ requestValidator.validateHttpMethod()               │
│  │  └─ Check: GET, POST, PUT, DELETE, PATCH, etc.      │
│  │     ❌ Invalid → throw CustomException 400           │
│  │                                                      │
│  ├─ requestValidator.validateUrl()                      │
│  │  ├─ Check: not empty, not too long (max 2048)       │
│  │  ├─ Check: http/https scheme only                    │
│  │  ├─ Check: valid URI syntax                          │
│  │  └─ ❌ Invalid → throw CustomException 400           │
│  │                                                      │
│  ├─ securityValidator.validateSSRF()                    │
│  │  ├─ Check: not localhost                             │
│  │  ├─ Check: not 127.0.0.1                             │
│  │  ├─ Check: not 192.168.x.x (private range)          │
│  │  ├─ Check: not 10.x.x.x (private range)             │
│  │  ├─ Resolve hostname & check resolved IP             │
│  │  └─ ❌ Blocked → throw CustomException 403           │
│  │                                                      │
│  └─ requestValidator.validateRequestBody()              │
│     ├─ Check: valid JSON syntax                         │
│     ├─ Check: size < 10MB                               │
│     └─ ❌ Invalid → throw CustomException 400           │
│                                                          │
│  PHASE 2: PREPARE REQUEST                               │
│  ───────────────────────                                │
│  ├─ Create HttpHeaders                                  │
│  ├─ Add custom headers from request                     │
│  ├─ Set Content-Type: application/json                  │
│  └─ Create HttpEntity with headers + body               │
│                                                          │
│  PHASE 3: EXECUTE HTTP REQUEST                          │
│  ──────────────────────────────                          │
│  ├─ Record start time: System.currentTimeMillis()       │
│  │                                                      │
│  ├─ Call external API:                                  │
│  │  restTemplate.exchange(                              │
│  │    url,                                              │
│  │    HttpMethod.GET,   (or PUT, POST, DELETE)         │
│  │    httpEntity,                                       │
│  │    String.class                                      │
│  │  )                                                   │
│  │                                                      │
│  ├─ Record end time: System.currentTimeMillis()         │
│  ├─ Calculate latency: endTime - startTime              │
│  │                                                      │
│  └─ Handle Exceptions:                                  │
│     └─ catch RestClientException → throw 502            │
│                                                          │
│  PHASE 4: SAVE TO HISTORY                               │
│  ────────────────────────                               │
│  ├─ Extract response headers                            │
│  ├─ Create HistoryEntry entity:                         │
│  │  ├─ user = authenticated user                        │
│  │  ├─ method = "GET"                                   │
│  │  ├─ url = "https://api.example.com/users"            │
│  │  ├─ requestBody = JSON string or null               │
│  │  ├─ requestHeaders = JSON serialized                 │
│  │  ├─ responseStatus = 200                             │
│  │  ├─ responseBody = response body                     │
│  │  ├─ responseHeaders = JSON serialized                │
│  │  ├─ latencyMs = calculated latency                   │
│  │  └─ createdAt = LocalDateTime.now()                  │
│  │                                                      │
│  └─ historyRepository.save(entry)                       │
│     └─ DB: INSERT INTO history_entries (...)            │
│                                                          │
│  PHASE 5: RETURN RESPONSE                               │
│  ───────────────────────                                │
│  └─ ApiResponseDTO with:                                │
│     ├─ historyId = saved entry ID                       │
│     ├─ statusCode = 200                                 │
│     ├─ body = response body                             │
│     ├─ headers = response headers map                   │
│     ├─ latencyMs = latency                              │
│     ├─ method = "GET"                                   │
│     └─ url = "https://api.example.com/users"            │
│                                                          │
└──────────────┬────────────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────────┐
│  HTTP Response 200 OK                                   │
│                                                          │
│  {                                                      │
│    "historyId": 42,                                     │
│    "statusCode": 200,                                   │
│    "body": "{\"users\": [{...}]}",                      │
│    "headers": {                                         │
│      "Content-Type": "application/json",                │
│      "Date": "Mon, 15 Apr 2024 10:30:00"                │
│    },                                                   │
│    "latencyMs": 245,                                    │
│    "method": "GET",                                     │
│    "url": "https://api.example.com/users"               │
│  }                                                      │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

---

## Data Persistence Flow

### 💾 History Retrieval Flow

```
┌─────────────────────────────────────┐
│   Client Request                    │
│                                     │
│   GET /api/history?page=0&size=20   │
│   Authorization: Bearer <token>     │
│                                     │
└────────┬────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│  HistoryController.getHistory()              │
│                                              │
│  Receives:                                   │
│  - page = 0 (1st page)                       │
│  - size = 20 (20 items per page)             │
│  - userId from JWT @RequestAttribute         │
│                                              │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│  HistoryRepository.findByUserOrderByCreatedAtDesc() │
│                                              │
│  JPA Query:                                  │
│  SELECT * FROM history_entries              │
│  WHERE user_id = ?                           │
│  ORDER BY created_at DESC                    │
│  LIMIT 20 OFFSET 0                           │
│                                              │
│  Result: Page<HistoryEntry>                 │
│  - 20 HistoryEntry objects                   │
│  - Total count: 1000 entries                 │
│  - Page info: 0/50 pages                     │
│                                              │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│  Map Entities to DTOs                        │
│                                              │
│  For each HistoryEntry:                      │
│  ├─ Deserialize JSON headers                 │
│  └─ Create HistoryEntryDTO                   │
│                                              │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│  HTTP Response 200 OK                        │
│                                              │
│  {                                           │
│    "data": [                                 │
│      {                                       │
│        "id": 42,                             │
│        "method": "GET",                      │
│        "url": "https://api.example.com/users"│
│        "responseStatus": 200,                │
│        "latencyMs": 245,                     │
│        "createdAt": "2024-04-15T10:30:00"    │
│      },                                      │
│      ...                                     │
│    ],                                        │
│    "totalCount": 1000,                       │
│    "pageNumber": 0,                          │
│    "pageSize": 20,                           │
│    "totalPages": 50                          │
│  }                                           │
│                                              │
└──────────────────────────────────────────────┘
```

### 🎯 Saved Requests Flow

```
┌──────────────────────────────────────┐
│  Client: Save Named Request          │
│                                      │
│  POST /api/storage/saved-requests    │
│  Authorization: Bearer <token>       │
│                                      │
│  {                                   │
│    "name": "Get Users",              │
│    "description": "User listing",    │
│    "method": "GET",                  │
│    "url": "https://api.example.com/users"│
│    "headers": {...}                  │
│  }                                   │
│                                      │
└────────┬─────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────┐
│  StorageController.saveRequest()     │
│  → StorageService.saveRequest()      │
│                                      │
│  Step 1: Validate                    │
│  ├─ Name not empty                   │
│  └─ URL validated                    │
│                                      │
│  Step 2: Create Entity               │
│  ├─ NamedRequest.builder()           │
│  │  ├─ user = authenticated user     │
│  │  ├─ name = "Get Users"            │
│  │  ├─ method = "GET"                │
│  │  ├─ url = URL                     │
│  │  ├─ requestHeaders = JSON encoded │
│  │  └─ createdAt = now               │
│  │                                   │
│  Step 3: Save to Database            │
│  └─ namedRequestRepository.save()    │
│     └─ INSERT INTO named_requests    │
│                                      │
│  Step 4: Map to DTO                  │
│  └─ NamedRequestDTO                  │
│                                      │
└────────┬─────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────┐
│  HTTP 201 Created                    │
│                                      │
│  {                                   │
│    "id": 5,                          │
│    "name": "Get Users",              │
│    "method": "GET",                  │
│    "url": "https://api.example.com/users"│
│    "createdAt": "2024-04-15T10:30:00"│
│  }                                   │
│                                      │
└──────────────────────────────────────┘
```

---

## Security & Validation Flow

### 🛡️ SSRF (Server-Side Request Forgery) Protection

```
┌──────────────────────────────────────────────────────┐
│  Malicious Requests Attempt                          │
│                                                      │
│  Attempt 1: localhost                                │
│  Attempt 2: 127.0.0.1                                │
│  Attempt 3: 192.168.1.1                              │
│  Attempt 4: 10.0.0.1                                 │
│  Attempt 5: 172.16.0.1                               │
│                                                      │
└──────────────┬───────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────┐
│  SecurityValidator.validateSSRF(url)                 │
│                                                      │
│  Phase 1: Parse URL                                  │
│  ├─ new URI(url)                                     │
│  ├─ Extract hostname: uri.getHost()                  │
│  └─ Validate hostname exists                         │
│                                                      │
│  Phase 2: Check Pattern Blacklist                    │
│  ├─ isPrivateIP(hostname):                           │
│  │  ├─ Check: equals "localhost" → BLOCK             │
│  │  ├─ Check: equals "127.0.0.1" → BLOCK             │
│  │  ├─ Check: startsWith "192.168." → BLOCK           │
│  │  ├─ Check: startsWith "10." → BLOCK                │
│  │  ├─ Check: startsWith "172." → BLOCK               │
│  │  ├─ Check: equals "::" (IPv6) → BLOCK              │
│  │  └─ Check: startsWith "fc00:", "fe80:" (IPv6) → BLOCK │
│  │                                                  │
│  │  Result: If blocked → throw 403 FORBIDDEN         │
│  │                                                  │
│  Phase 3: Resolve Hostname                           │
│  ├─ InetAddress.getAllByName(hostname)               │
│  ├─ For each resolved IP address:                    │
│  │  ├─ addr.isLoopbackAddress() → BLOCK              │
│  │  ├─ isResolvedIPPrivate(addr) → BLOCK             │
│  │  ├─ addr.isAnyLocalAddress() → BLOCK              │
│  │  └─ addr.isLinkLocalAddress() → BLOCK             │
│  │                                                  │
│  │  Result: If blocked → throw 403 FORBIDDEN         │
│  │                                                  │
│  └─ If passes all checks:                            │
│     └─ Continue with request                         │
│                                                      │
└──────────────┬───────────────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────────────────┐
│  Result                                              │
│                                                      │
│  ✅ https://jsonplaceholder.typicode.com → ALLOW     │
│  ✅ https://api.github.com → ALLOW                   │
│  ❌ http://localhost:8080 → 403 FORBIDDEN            │
│  ❌ http://192.168.1.1 → 403 FORBIDDEN               │
│  ❌ http://10.0.0.1 → 403 FORBIDDEN                  │
│                                                      │
└──────────────────────────────────────────────────────┘
```

### ✅ Input Validation Flow

```
┌────────────────────────────────────────────────────┐
│  RequestValidator.validateUrl()                    │
│                                                    │
│  1. URL Not Empty                                  │
│     ├─ if (url == null || url.isEmpty())           │
│     └─ throw "URL cannot be empty" (400)            │
│                                                    │
│  2. URL Length Check                               │
│     ├─ Max 2048 characters                         │
│     └─ if (url.length() > 2048)                    │
│        └─ throw "URL exceeds max length" (400)     │
│                                                    │
│  3. URI Syntax Check                               │
│     ├─ new URI(url)                                │
│     └─ if (URISyntaxException)                     │
│        └─ throw "Invalid URL format" (400)         │
│                                                    │
│  4. Scheme Validation                              │
│     ├─ uri.getScheme()                             │
│     ├─ if (!scheme.equals("http|https"))           │
│     └─ throw "URL must use http or https" (400)    │
│                                                    │
│  5. Host Validation                                │
│     ├─ uri.getHost()                               │
│     ├─ if (host == null)                           │
│     └─ throw "URL must contain valid host" (400)   │
│                                                    │
└─────────────────────────────────────┬──────────────┘
                                       │
                                       ▼
┌────────────────────────────────────────────────────┐
│  RequestValidator.validateRequestBody()            │
│                                                    │
│  1. Body Optional but if Present:                  │
│     ├─ Check size < 10MB                           │
│     └─ throw "Payload too large" (400)             │
│                                                    │
│  2. JSON Syntax Validation                         │
│     ├─ Count braces: { }                           │
│     ├─ Count brackets: [ ]                         │
│     ├─ if (counts don't match)                     │
│     └─ throw "Invalid JSON format" (400)           │
│                                                    │
│  3. Result                                         │
│     ├─ ✅ {} → VALID                               │
│     ├─ ✅ {"key": "value"} → VALID                 │
│     ├─ ✅ [] → VALID                               │
│     ├─ ❌ {incomplete → INVALID                    │
│     └─ ❌ {"]"} → INVALID                          │
│                                                    │
└────────────────────────────────────────────────────┘
```

---

## Module Interactions

### 🔄 Complete User Journey

```
Timeline View of a Complete User Session:

T=0s    User Registration
        ┌──────────────────────┐
        │ POST /auth/register  │
        │ username/email/pwd   │
        └─────────┬────────────┘
                 │ (HTTP)
                 ▼
        ┌────────────────────────────┐
        │ ① AuthController           │
        │ ② AuthService.register()   │
        │ ③ UserRepository.save()    │
        │ ④ JwtService.generateToken │
        │ ⑤ Response + JWT token     │
        └─────────┬────────────────┘
                 │ JWT: "eyJh..."
                 ▼
        ✅ User token stored client-side

T=5s    User Makes API Request
        ┌──────────────────────┐
        │ POST /request/execute│
        │ URL + headers + token│
        └─────────┬────────────┘
                 │ (HTTP + Bearer Token)
                 ▼
        ┌────────────────────────────┐
        │ ① JwtFilter validates JWT  │
        │ ② Extract userId + context │
        │ ③ Pass to Controller       │
        └─────────┬────────────────┘
                 │ userId set
                 ▼
        ┌────────────────────────────┐
        │ ApiController.executeRequest│
        │ Get User from DB            │
        └─────────┬────────────────┘
                 │
                 ▼
        ┌────────────────────────────┐
        │ ApiService.executeRequest() │
        │ ① Validate request         │
        │ ② Check SSRF (host IP)     │
        │ ③ Make external API call   │
        │ ④ Capture response         │
        │ ⑤ Save to history          │
        └─────────┬────────────────┘
                 │
                 ▼
        ┌────────────────────────────┐
        │ HistoryRepository.save()   │
        │ INSERT into database       │
        └─────────┬────────────────┘
                 │
                 ▼
        ✅ Response returned to client

T=10s   User Views History
        ┌──────────────────────┐
        │ GET /api/history     │
        │ token                │
        └─────────┬────────────┘
                 │
                 ▼
        ┌────────────────────────────┐
        │ ① JwtFilter validates JWT  │
        │ ② Extract userId           │
        └─────────┬────────────────┘
                 │
                 ▼
        ┌────────────────────────────┐
        │ HistoryController.getHistory │
        │ page=0, size=20             │
        └─────────┬────────────────┘
                 │
                 ▼
        ┌────────────────────────────┐
        │ HistoryRepository          │
        │ findByUserOrderByCreatedAtDesc │
        │ SELECT * WHERE user_id = ? │
        └─────────┬────────────────┘
                 │
                 ▼
        ✅ History list returned (20 items)

T=15s   User Saves Request
        ┌──────────────────────┐
        │ POST /storage/saved- │
        │ requests             │
        │ name + url + token   │
        └─────────┬────────────┘
                 │
                 ▼
        ┌────────────────────────────┐
        │ StorageController          │
        │ SaveRequest                │
        └─────────┬────────────────┘
                 │
                 ▼
        ┌────────────────────────────┐
        │ StorageService.saveRequest()│
        │ Validate                   │
        │ Create NamedRequest entity  │
        │ Save to DB                 │
        └─────────┬────────────────┘
                 │
                 ▼
        ✅ Saved request persisted
```

---

## Database Schema

### 📊 Entity Relationships

```
┌──────────────────┐
│     users        │  (Main User Account)
├──────────────────┤
│ id (PK)          │
│ username (UQ)    │
│ email (UQ)       │
│ passwordHash     │
│ created_at       │
│ updated_at       │
└─────────┬────────┘
          │ 
          │ 1:N (One user to many entries)
          │
    ┌─────┴─────┐
    │           │
    ▼           ▼
┌────────────────────────┐  ┌──────────────────────┐
│  history_entries       │  │  named_requests      │
├────────────────────────┤  ├──────────────────────┤
│ id (PK)                │  │ id (PK)              │
│ user_id (FK)           │  │ user_id (FK)         │
│ method                 │  │ name                 │
│ url                    │  │ method               │
│ request_body           │  │ url                  │
│ request_headers (JSON) │  │ request_body         │
│ response_status        │  │ request_headers (JSON)│
│ response_body          │  │ description          │
│ response_headers (JSON)│  │ created_at           │
│ latency_ms             │  │ updated_at           │
│ created_at             │  └──────────────────────┘
└────────────────────────┘

    ┌──────────────┬─────────────────┐
    │              │                 │
    ▼              ▼                 ▼
┌──────────────────────┐  ┌─────────────────────┐
│   collections        │  │  request_jobs       │
├──────────────────────┤  │  (Phase 4 - Ready)  │
│ id (PK)              │  ├─────────────────────┤
│ user_id (FK)         │  │ id (PK)             │
│ name                 │  │ user_id (FK)        │
│ description          │  │ status (ENUM)       │
│ created_at           │  │ method              │
│ updated_at           │  │ url                 │
└──────────────────────┘  │ request_body        │
                          │ response_status     │
                          │ response_body       │
                          │ latency_ms          │
                          │ error_message       │
                          │ created_at          │
                          │ started_at          │
                          │ completed_at        │
                          └─────────────────────┘
```

### 🔑 Key Relationships

```
User (1) ──────── (N) HistoryEntry
├─ Each history entry belongs to ONE user
├─ Each user can have MANY history entries
└─ User deletion cascades to history (business rule)

User (1) ──────---- (N) NamedRequest
├─ Each saved request belongs to ONE user
├─ Each user can have MANY saved requests
└─ Only user can access their saved requests

User (1) ────────── (N) Collection
├─ Each collection belongs to ONE user
├─ Each user can have MANY collections
└─ Collection groups organize saved requests

User (1) ──────---- (N) RequestJob
├─ Each job belongs to ONE user
├─ For Phase 4: async request processing
└─ (Reserved for future implementation)
```

---

## Exception Handling Flow

### ⚠️ Error Handling Pipeline

```
┌────────────────────────────────────────────────┐
│  Exception Thrown Anywhere in System           │
│                                                 │
│  Examples:                                      │
│  - CustomException("Invalid URL", "...", 400)  │
│  - IllegalArgumentException                    │
│  - RestClientException                         │
│  - Any other Exception                          │
│                                                 │
└──────────────┬──────────────────────────────────┘
               │
               ▼
┌────────────────────────────────────────────────┐
│  GlobalExceptionHandler                        │
│  @RestControllerAdvice (catches ALL)           │
│                                                 │
│  Routing:                                       │
│  ├─ if (CustomException)                        │
│  │  └─ handleCustomException()                  │
│  │     ├─ Extract code, message, status        │
│  │     └─ Return ErrorResponse                  │
│  │                                              │
│  └─ else                                        │
│     └─ handleGeneralException()                │
│        ├─ Log unexpected error                 │
│        └─ Return generic 500 response          │
│                                                 │
└──────────────┬──────────────────────────────────┘
               │
               ▼
┌────────────────────────────────────────────────┐
│  ErrorResponse (JSON)                          │
│                                                 │
│  {                                             │
│    "code": "INVALID_URL",                      │
│    "message": "URL must use http or https",    │
│    "status": 400,                              │
│    "timestamp": "2024-04-15T10:30:00"          │
│  }                                             │
│                                                 │
│  HTTP Response with appropriate status code:  │
│  - 400 Bad Request (validation errors)         │
│  - 401 Unauthorized (auth failures)            │
│  - 403 Forbidden (access denied/SSRF blocked)  │
│  - 404 Not Found (resource missing)            │
│  - 409 Conflict (duplicate user)               │
│  - 500 Internal Server Error (unexpected)      │
│                                                 │
└────────────────────────────────────────────────┘
```

---

## 🎯 Key Design Patterns

### 1. **Layered Architecture**
```
Presentation Layer (Controllers)
        ↓
Application Layer (Services)
        ↓
Data Access Layer (Repositories)
        ↓
Database Layer (PostgreSQL)
```

### 2. **Dependency Injection**
- Spring automatically injects all dependencies
- Controllers receive Services
- Services receive Repositories
- Reduces coupling, improves testability

### 3. **DTO Pattern**
- DTOs for API contracts (external)
- Entities for database (internal)
- Mapping layer between them
- Allows API changes without DB schema changes

### 4. **Repository Pattern**
- JPA handles database interactions
- Services don't know SQL details
- Easy to test with mocked repositories

### 5. **Validation at Multiple Layers**
- **HTTP Level:** JWT token validation
- **Input Level:** RequestValidator checks
- **Security Level:** SecurityValidator checks
- **Database Level:** NOT NULL, UNIQUE constraints

---

## 📥 HTTP Request Flow Summary

```
1. HTTP Request arrives
        ↓
2. Spring DispatcherServlet routes to JwtFilter
        ↓
3. JwtFilter validates JWT token
        ↓
4. Request routed to appropriate Controller
        ↓
5. Controller receives @RequestAttribute(userId)
        ↓
6. Controller calls Service
        ↓
7. Service validates input (RequestValidator)
        ↓
8. Service validates security (SecurityValidator)
        ↓
9. Service performs business logic
        ↓
10. Service calls Repository (JPA)
        ↓
11. Repository translates to SQL query
        ↓
12. PostgreSQL executes query
        ↓
13. Results mapped back to Java objects
        ↓
14. Service transforms to DTO
        ↓
15. Controller returns response
        ↓
16. Spring converts DTO to JSON
        ↓
17. HTTP Response sent to client
```

---

## 🚀 Summary

This document provides complete visibility into:
- ✅ How each module connects to others
- ✅ Complete request/response flows
- ✅ Database interactions
- ✅ Security validation pipeline
- ✅ Error handling mechanism
- ✅ Data transformation steps

**Key Takeaway:** The system is built on proven enterprise patterns (layered architecture, DTOs, dependency injection) ensuring scalability, testability, and maintainability.

---

**Last Updated:** April 15, 2026  
**Architecture Version:** 1.0 (Phases 1-2)  
**Next Update:** After Phase 3 implementation
