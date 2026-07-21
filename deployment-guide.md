# ProbeAPI Deployment Guide

This guide covers a practical deployment for the current project layout:
- Backend: Spring Boot app in `ApiDashboard`
- Frontend: React app in `frontend`

## 1) Pre-Deployment Checklist

- Backend builds successfully:
  - `cd ApiDashboard && mvn clean install`
- Frontend builds successfully:
  - `cd frontend && npm install && npm run build`
- JWT secret is changed from default
- API base URL in frontend is set correctly
- CORS allowed origins are reviewed for production domain

## 2) Backend Deployment (Render/Railway/VM)

### Required environment variables

Set these in your backend hosting platform:

- `SERVER_PORT` (or platform `PORT`)
- `JWT_SECRET` (strong random value)
- `SPRING_PROFILES_ACTIVE=prod` (recommended)
- `SPRING_DATASOURCE_URL` (if using PostgreSQL)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### Build and run command

```bash
cd ApiDashboard
mvn clean package
java -jar target/ApiDashboard-0.0.1-SNAPSHOT.jar
```

### Health check endpoint

Use:
- `GET /api/auth/health`

## 3) Frontend Deployment (Vercel/Netlify)

Set environment variable:

- `REACT_APP_API_URL=https://<your-backend-domain>/api`

Build command:

```bash
cd frontend
npm install
npm run build
```

Publish directory:
- `frontend/build`

## 4) Recommended Production DB Setup

Current local setup commonly uses H2 for quick development.
For production, use PostgreSQL:

- Create DB and user
- Set datasource env vars
- Keep `ddl-auto` conservative in production (prefer migrations)

## 5) Security Hardening Checklist

- Use a long random JWT secret
- Restrict CORS to frontend domain only
- Enforce HTTPS
- Avoid logging sensitive auth details
- Set logging to `INFO` or `WARN` in production
- Enable DB backups

## 6) Smoke Test After Deploy

1. `GET /api/auth/health`
2. Register user (`POST /api/auth/register`)
3. Login user (`POST /api/auth/login`)
4. Execute request (`POST /api/request/execute`)
5. Verify history (`GET /api/history`)
6. Verify saved request CRUD and collection CRUD

If these pass, deployment is good.
