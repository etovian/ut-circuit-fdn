# Plan: Circuit-Wide Events Page Implementation

## Overview
Create a dedicated page for circuit-wide events accessible from the Utah Circuit Hub navigation. This involves extending the existing event system to support a "circuit-wide" flag and exposing a filtered view of these events.
- [x] Phase 1: Database Migration
    - [x] Create Liquibase changelog `015-add-is-circuit-event-to-events.xml`.
    - [x] Register the new changelog in `db.changelog-master.xml`.

- [x] Phase 2: Backend Implementation
    - [x] **Entities**: Updated `EventTemplateEntity` and `ScheduledEventInstanceEntity`.
    - [x] **DTOs**: Updated `EventTemplateDto` and `EventOccurrenceDto`.
    - [x] **Repositories**: Updated `ScheduledEventInstanceRepository`.
    - [x] **Service**: Updated `EventService` mapping, creation, and update logic; added `getCircuitScheduledInstances`.
    - [x] **Controller**: Added `GET /api/events/circuit/scheduled/next-thirty-days`.

- [x] Phase 3: Frontend Implementation
    - [x] **Models**: Updated `event.model.ts`.
    - [x] **Service**: Updated `event.service.ts` with `getCircuitEvents()` calling the 30-day endpoint.
    - [x] **Components**: Created `CircuitEvents` component.
    - [x] **Routing & Navigation**: Added `/events` route and updated navigation link.

- [x] Phase 4: Validation & Seeding
    - [x] Updated `seed-events.sql` with a circuit-wide event.
    - [x] Verified backend integrity with `./gradlew test`.

## Checklist Progress
- [x] Phase 1: Database Migration
- [x] Phase 2: Backend Implementation
- [x] Phase 3: Frontend Implementation
- [x] Phase 4: Validation

