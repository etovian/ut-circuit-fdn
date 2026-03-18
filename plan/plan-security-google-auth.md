# Implementation Detail: Google OAuth2 Authentication

## Objective
This document provides the specific configuration and implementation details for integrating Google as an OAuth2 authentication provider, as outlined in the [Main Security Plan](./plan-security-authentication.md).

## Prerequisites (Google Cloud Console)
1.  **Create Project**: A dedicated project in the [Google Cloud Console](https://console.cloud.google.com/).
2.  **Configure OAuth Consent Screen**:
    *   User Type: External.
    *   Scopes: `.../auth/userinfo.email`, `.../auth/userinfo.profile`, `openid`.
3.  **Create Credentials**:
    *   Type: OAuth 2.0 Client ID.
    *   Application Type: Web Application.
    *   **Authorized Redirect URIs**:
        *   Development: `http://localhost:8080/login/oauth2/code/google`
        *   Production: `https://[your-domain]/login/oauth2/code/google`
4.  **Secrets Management**: Store the `Client ID` and `Client Secret` securely (e.g., environment variables).

## Backend Configuration

### 1. Spring Boot Properties (`src/main/resources/application.properties`)
```properties
# Google OAuth2 Client Configuration
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
```

### 2. Provider-Specific Mapping Logic
In the custom `OAuth2UserService` (see Phase 1, Step 4 of the main plan):

*   **Registration ID**: `google`
*   **User Attribute ID**: `sub` (Google's unique identifier for the user).
*   **Email Attribute**: `email`
*   **Name Attributes**: `given_name` (First Name), `family_name` (Last Name).

### 3. Implementation Logic
```java
// Logic within CustomOAuth2UserService.loadUser()
OAuth2User oAuth2User = super.loadUser(userRequest);
String email = oAuth2User.getAttribute("email");
String providerId = oAuth2User.getAttribute("sub"); // Google's unique 'sub'

UserEntity user = userRepository.findByEmail(email)
    .orElseThrow(() -> new OAuth2AuthenticationException("User not pre-registered: " + email));

// Link Google ID on first successful login
if (user.getOauth2Id() == null) {
    user.setOauth2Provider("google");
    user.setOauth2Id(providerId);
    userRepository.save(user);
}
```

## Frontend Integration

### 1. Login Trigger
The frontend will provide a dedicated Google login button:
```html
<!-- login.component.html -->
<a href="/oauth2/authorization/google" class="google-login-btn">
  <img src="assets/google-logo.svg" alt="Google Logo">
  Sign in with Google
</a>
```

### 2. User Data Handling
Once authenticated, the Google profile picture URL (available in the `picture` attribute of the OAuth2 user) can be used to update the `PersonEntity` if it's currently missing a photo.

## Verification
1.  Verify that the `state` parameter is present in the redirect to prevent CSRF.
2.  Ensure that if a user revokes access via their Google Account, subsequent login attempts correctly trigger the error flow.
3.  Test with a Google account whose email does **not** exist in the `app_user` table to confirm the "Pre-registration Required" policy.
