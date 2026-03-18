# Plan: Introducing Spring Security and OAuth2 Authentication (Revised: Separate User/Person)

## Objective
Enable secure access to the UT Circuit FDN application by integrating Spring Security and supporting social logins (Google, Facebook, etc.) via OAuth2. This plan separates the Authentication concerns (`User`) from the Identity concerns (`Person`).

## Key Files & Context
- `build.gradle`: New dependencies for security and OAuth2.
- `src/main/java/com/utcfdn/config/SecurityConfig.java`: Central security configuration.
- `src/main/java/com/utcfdn/auth/UserEntity.java`: New entity for authentication data.
- `src/main/java/com/utcfdn/auth/UserDto.java`: Flattened DTO containing both User and Person data.
- `src/main/java/com/utcfdn/person/PersonEntity.java`: Existing identity entity.
- `src/main/resources/application.properties`: Configuration for OAuth2 clients.
- `frontend/src/app/auth/`: New authentication service and component.

## Implementation Steps

### Phase 1: Backend Infrastructure
1.  **Add Dependencies**:
    *   `org.springframework.boot:spring-boot-starter-security`
    *   `org.springframework.boot:spring-boot-starter-oauth2-client`
2.  **Database Updates (Liquibase)**:
    *   Create an `app_user` table:
        *   `id` (PK)
        *   `person_id` (FK to `person.id`, Unique)
        *   `email` (Unique)
        *   `oauth2_provider` (e.g., 'google', 'facebook')
        *   `oauth2_id` (Unique ID from provider)
        *   `role` (e.g., 'ROLE_USER', 'ROLE_ADMIN', 'ROLE_CIRCUIT_ADMIN')
3.  **Update Domain Models**:
    *   **UserEntity**: Create new entity to map to `app_user`.
        *   Add `@OneToOne` relationship to `PersonEntity`.
    *   **UserPrincipal**: Implement `UserDetails` and `OAuth2User`, wrapping `UserEntity`.
    *   **UserDto**: Create a flattened DTO containing:
        *   User fields: `id`, `email`, `role`, `oauth2Provider`.
        *   Person fields: `firstName`, `lastName`, `title`, `photoFileName`, etc.
4.  **Security Configuration**:
    *   Implement `SecurityConfig` to define public and private endpoints.
    *   Configure `oauth2Login()` with a custom `OAuth2UserService`.
        *   Logic: Find `UserEntity` by `email` (from OAuth2 provider).
        *   If `UserEntity` is **not found**, authentication fails (access denied). Users must be pre-registered by an administrator (`ROLE_ADMIN` or `ROLE_CIRCUIT_ADMIN`).
        *   If `UserEntity` is found:
            *   On first login: Link the `oauth2_provider` and `oauth2_id` from the provider to the existing `UserEntity`.
            *   Update any missing or changed user properties.
            *   Establish the session.
    *   Enable CSRF protection with `CookieCsrfTokenRepository` for Angular compatibility.
5.  **Success Handler**:
    *   Implement a `SimpleUrlAuthenticationSuccessHandler` to redirect to the frontend after successful social login.

### Phase 2: Frontend Integration
1.  **Authentication Service**:
    *   Create `AuthService` in Angular to check current login status (via `/api/auth/me` returning the flattened `UserDto`).
    *   Manage the authentication state using a signal.
2.  **Login Component**:
    *   Provide a simple UI with social login buttons redirecting to `/oauth2/authorization/{provider}`.
3.  **Auth Guard**:
    *   Create an `AuthGuard` to protect administrative routes.
4.  **Interceptors**:
    *   Ensure `withCredentials: true` is set for all API calls.

## Verification & Testing
1.  **Unit Tests**:
    *   Verify `UserDto` correctly flattens data from both entities.
    *   Test `OAuth2UserService` correctly maps or creates linked entities.
2.  **Integration Tests**:
    *   Simulate OAuth2 login with mock provider and verify `app_user` and `person` records.
3.  **Manual Verification**:
    *   Verify login flow for a person who already exists in the database vs. a new person.
    *   Verify access control based on roles in `UserEntity`.
