# Frontend

## 1. Admin - Create Person [DONE]

- Create a reusable component that will allow users to create a new person record or edit an existing one.

## 2. Admin - Create Congregation Person [DONE]

- Create an admin page that will allow users to create a new person record or edit an existing one.  It should leverage the reusable component from #1.
- In addition, this component will have a field for "position".
- When the user saves a record through this interface, it will upsert a person record and associate that person with the congregation in the position specified.

## 3. Admin - Person Search and Select [DONE]

- Create a component that will allow a user to search for a person.  As the user types in the search fields, the keydown events will be debounced and then applied as search criteria.
- Person records that meet the search criterial will be displayed in a list.
- Clicking a person in the list will cause a "person event" to bubble up out of the component, so that it can be used in different contexts.

# Backend

## 1. Search Endpoint [DONE]

- Create an endpoint that handles person searches
- Create a person search service that will act as a delegate for the controller
- Spring Framework's ExampleMatcher may be a good candidate for this.

# Implementation Plan [COMPLETED]

## Phase 1: Backend Implementation [DONE]
1.  **PersonSearchService**: Implement a new service with a `search` method that uses `ExampleMatcher` to perform flexible matching (ignore case, matching ANY, contains) on `PersonEntity` properties like `firstName`, `lastName`, and `middleName`.
2.  **PersonController**: Add a new `@GetMapping("/search")` endpoint that takes query parameters and delegates search to `PersonSearchService`.

## Phase 2: Frontend Foundation [DONE]
1.  **Models and Services**: 
    - Create `person.model.ts` to define the `Person` and `CongregationPerson` interfaces.
    - Create `person.service.ts` with methods for:
        - `getPersons()`: Fetch all persons.
        - `searchPersons(query: string)`: Search persons using the new backend endpoint.
        - `createPerson(person: Person)`: Create a new person.
        - `updatePerson(id: number, person: Person)`: Update an existing person.
        - `addPersonToCongregation(congregationId: number, person: Person, position: string)`: Associate a person with a congregation.

## Phase 3: Frontend Component Development [DONE]
1.  **`PersonFormComponent`**: Create a standalone component for editing person details (title, first name, middle name, last name, suffix, biography, etc.).
2.  **`PersonSearchComponent`**: Create a standalone component that uses a debounced input to search for persons and emits a `personSelected` event.
3.  **`CongregationPersonAdminComponent`**: Create a new page component:
    - Located at `admin/congregation/:slug/persons`.
    - Features a search interface to find existing persons.
    - Features the `PersonFormComponent` for creating a new person or editing the selected one.
    - Includes a "Position" field to define the person's role in the congregation.
    - Logic to "save" which upserts the person and establishes the relationship.

## Phase 4: Integration and Routing [DONE]
1.  **App Routes**: Register the new admin route in `app.routes.ts`.
2.  **Navigation**: Add an "Administrate People" link to the congregation detail or main navigation for authorized users.
