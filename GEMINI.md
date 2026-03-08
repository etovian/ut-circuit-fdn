# UT Circuit FDN - Project Context

## Purpose

This application is a hub-and-spoke web application.  The Utah Circuit of the Lutheran Church Missouri Synod is the hub.  It should embrace the following design principles: 
- Use a “Federated” model that supports separate congregational sites that pull data from a Circuit hub.
- Pastors and Board members of individual congregations should have access to content that is pertinent to their own congregations, but assume that most of these types of administrators will lack technical acumen.  Therefore, a top-level administrator should be able to make Circuit-wide changes.
- Existing Infrastructure: Some congregations already have existing websites and domains that may need to be migrated.
- Beyond spiritual content, there are other potential requirements:
  - Hosting links to worship videos (e.g., YouTube)
  - Shared event calendars
  - Management of external links and resources (e.g., sermon uploads)
- Administrative Capacity: day-to-day updates should at first be handled by administrators with relevant technical experience.  Eventually, these first administrators should be able to train administrators for each congregation.  User administration interfaces should be very simple.

## Participating Congregations:

- Cross of Christ in Bountiful
- Redeemer in Salt Lake City
- St. John in Salt Lake City
- Grace in Sandy
- Grace in Spanish Fork (a satellite congregation of Grace in Sandy)
- Grace in Moab
- Holy Trinity in Logan
- St. Paul in Ogden
- Christ in Murray
- Holy Trinity in Riverton
- Trinity in St. George
- Good Shepherd in Richfield
- First in Tooele

## Tech Stack

This project is a full-stack application consisting of a Spring Boot backend and an Angular frontend.

## Project Overview

- **Backend**: A Spring Boot application (v3.4.2) using Java 21. It manages the business logic and provides REST endpoints.
- **Frontend**: A modern Angular application (v21.2.0) built with standalone components and TypeScript.
- **Purpose**: A foundation for building "circuit" based utilities, currently including basic health check monitoring and boilerplate structure.

## Architecture

- **Backend**: Organized by domain-driven package structure (`com.utcfdn.congregation`, `com.utcfdn.sermon`, etc.). Uses Gradle for builds.
- **Frontend**: Located in the `/frontend` directory. Follows Angular standalone component architecture.
- **Integration**: During development, the Angular CLI's proxy (configured in `proxy.conf.json`) routes `/api` requests to the backend at `http://localhost:8080`.

## Building and Running

### Backend (Root Directory)
- **Run**: `./gradlew bootRun` (Note: README mentions `mvn`, but `build.gradle` is present and active).
- **Test**: `./gradlew test`
- **Build**: `./gradlew build`

### Frontend (`/frontend` Directory)
- **Run**: `npm start` (alias for `ng serve`)
- **Build**: `node node_modules/@angular/cli/bin/ng build`
- **Test**: `npm test`

## Development Conventions

- **Component Style**: Frontend uses Angular standalone components.  Each component should have separate template and style files.
- **Data Handling**: Frontend utilizes Angular's `signal` for reactive state management.
- **API Communication**: The backend exposes `/api/health` for monitoring. The frontend uses `HttpClient` with `provideHttpClient()` in `app.config.ts`.
- **Styling**: Prefers Vanilla CSS with modern oklch colors and gradients, following the boilerplate aesthetic.  Web component style should be modern but reflect the dignity, tradition, and importance of liturgical worship.
- **File Naming**:
    - Backend: PascalCase for Java classes (`HealthController.java`).
    - Frontend: kebab-case for component selectors (`app-health`), but standalone component filenames are often simple (`health.ts`, `home.ts`).
- **Caution**: Avoid use of deprecated features, when possible.

## Key Files
- `src/main/java/com/utcfdn/UtCircuitFdnApplication.java`: Backend entry point.
- `src/main/java/com/utcfdn/controller/HealthController.java`: Example REST controller.
- `frontend/src/app/app.routes.ts`: Main frontend routing configuration.
- `frontend/src/app/health.ts`: System health monitoring component.
- `frontend/proxy.conf.json`: Development proxy configuration for API calls.
