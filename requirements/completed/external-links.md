# External Links

## Backend
- [x] Create an enum titled ExternalLinkType with the following values:
  - WEBSITE
  - FACEBOOK
  - YOUTUBE
  - SUBSTACK
- [x] Create a new table with the following fields:
  - id
  - congregation_id
  - title
  - description
  - external_link_type (string value of enum)
  - url
  - ordinal_value (for user-defined sorting, default to 0 or highest existing congregation value + 1)
- [x] Create a CongregationExternalLinkEntity for this table
- [x] Create a CongregationExternalLinkDto 
- [x] Create a CongregationExternalLinkMapper that maps the entity to a dto and vice versa
- [x] Create a CongregationExternalLinkService that 
  - handles CRUD operations for these links
  - returns all links associated with a congregation, sorted by ordinal value
- [x] Create a CongregationExternalLinkController that delegates API requests to CongregationExternalLinkService

## Frontend
- [x] Add a section to the congregation detail page between "About Our Congregation" and "Our Leadership & Staff" that 
  - displays the congregation's external links
  - displays an icon that expresses a link's ExternalLinkType
- [x] Add a "Manage Links" button to this new section that redirects to a "Manage Links for {congregation}" page
- [x] The "Manage Links for {congregation}" page should be similar to the "Manage People" page

# Implementation Plan

## Phase 1: Backend Infrastructure
1.  **Model & Enum**: 
    - Create `com.utcfdn.congregation.ExternalLinkType` enum.
    - Create `com.utcfdn.congregation.CongregationExternalLinkEntity`.
    - Update `CongregationEntity` to include a `@OneToMany` relationship with `CongregationExternalLinkEntity`.
2.  **Database**: Create Liquibase changelog `013-external-link-table.xml` for the new table.
3.  **Repository**: Create `com.utcfdn.congregation.CongregationExternalLinkRepository` with native SQL query for ordered fetch.
4.  **DTO & Mapper**: 
    - Create `com.utcfdn.congregation.CongregationExternalLinkDto`.
    - Create `com.utcfdn.congregation.CongregationExternalLinkMapper`.
5.  **Service**: Create `com.utcfdn.congregation.CongregationExternalLinkService` for CRUD and reordering.
6.  **Controller**: Create `com.utcfdn.congregation.CongregationExternalLinkController` with endpoints for CRUD and reordering.

## Phase 2: Frontend Implementation
1.  **Model & Service**:
    - Create `external-link.model.ts` and `external-link.service.ts` in `frontend/src/app/congregation/`.
2.  **Congregation Detail Page**:
    - Update `congregation-detail.html` to add the "External Links" section with type-specific icons (e.g., using SVG or FontAwesome-style placeholders).
    - Add "Manage Links" button in the admin actions area within this section.
3.  **Management Component**:
    - Create `congregation-external-link-admin` component.
    - Implement a list view with reordering buttons and an edit/delete interface.
    - Implement a modal for creating/editing links.
4.  **Routing**: Add the route `admin/congregation/:slug/links` to `app.routes.ts`.
5.  **Toasts**: Integrate with the new `ToastService` for success/error notifications.

  