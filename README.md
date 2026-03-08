# UT Circuit FDN

This project consists of a Spring Boot backend and an Angular frontend.

## Project Structure

- `/`: Spring Boot Backend (Maven)
- `/frontend/`: Angular Frontend

## Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 24+
- Angular CLI 19+ (installed via npx)

## Running the Backend

From the root directory:
```bash
mvn spring-boot:run
```
The backend will be available at `http://localhost:8080`.
API Health check: `http://localhost:8080/api/health`

## Running the Frontend

From the `frontend/` directory:
```bash
npm install
npm start
```
The frontend will be available at `http://localhost:4200`.

## Integration

The frontend and backend are currently separate. For development, you can use the Angular proxy to route `/api` requests to the backend.
