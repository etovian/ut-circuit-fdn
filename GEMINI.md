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

- **Backend**: Uses Gradle for builds.
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
- **Search Tools**: `ripgrep` (rg) and `fzf` are installed via winget and should be used for efficient searching and filtering. Use their absolute paths if they are not available in the current shell session's PATH:
  - `rg`: `C:\Users\Michael Green\AppData\Local\Microsoft\WinGet\Packages\BurntSushi.ripgrep.MSVC_Microsoft.Winget.Source_8wekyb3d8bbwe\ripgrep-15.1.0-x86_64-pc-windows-msvc\rg.exe`
  - `fzf`: `C:\Users\Michael Green\AppData\Local\Microsoft\WinGet\Packages\junegunn.fzf_Microsoft.Winget.Source_8wekyb3d8bbwe\fzf.exe`
- **Environment Interaction**: Connecting directly to the container (e.g., via `docker exec`) to perform non-development functions is preferred over writing ad hoc tests that will be deleted after execution.
- **Caution**: Avoid use of deprecated features, when possible.
- **Project Structure**: Organize with domain-driven package structure (`com.utcfdn.congregation`, `com.utcfdn.sermon`, etc.).
- **SOLID Principles**: Embrace them.  Always suggest ways to refactor a class that has encompassed too many concerns.

### Frontend
- **Component Style**: Frontend uses Angular standalone components.  Each component should have separate template and style files.
- **Data Handling**: Frontend utilizes Angular's `signal` for reactive state management.
- **File Naming**: kebab-case for component selectors (`app-health`), but standalone component filenames are often simple (`health.ts`, `home.ts`).
 
### Backend
- **API Communication**: The backend exposes `/api/health` for monitoring. The frontend uses `HttpClient` with `provideHttpClient()` in `app.config.ts`.
- **Styling**: Prefers Vanilla CSS with modern oklch colors and gradients, following the boilerplate aesthetic.  Web component style should be modern but reflect the dignity, tradition, and importance of liturgical worship.
- **File Naming**: PascalCase for Java classes (`HealthController.java`).
- **Database Queries**: Use parameterized native SQL queries.  Avoid use of JQL and dynamic repository queries (e.g., findAllByOrderByNameAsc).
- **Testing**: When adding new features, create tests that thoroughly cover them.  When change code with test coverage, execute the relevant tests.

## Key Files
- `src/main/java/com/utcfdn/UtCircuitFdnApplication.java`: Backend entry point.
- `src/main/java/com/utcfdn/controller/HealthController.java`: Example REST controller.
- `frontend/src/app/app.routes.ts`: Main frontend routing configuration.
- `frontend/src/app/health.ts`: System health monitoring component.
- `frontend/proxy.conf.json`: Development proxy configuration for API calls.

## Requirements
- When given a requirements file to process, implement all the unmet requirements listed in it.  Be sure to report your progress and update each requirement in the file when you are finished with the implementation.
- Whenever asked to create a plan or requirements document, you will write a .md file to the /plan directory.
