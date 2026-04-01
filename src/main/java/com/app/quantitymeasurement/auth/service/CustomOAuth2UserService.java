package com.app.quantitymeasurement.auth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.quantitymeasurement.auth.entity.AuthProvider;
import com.app.quantitymeasurement.auth.entity.User;
import com.app.quantitymeasurement.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CustomOAuth2UserService.java
 *
 * This service is called by Spring Security when a user authenticates through Google OAuth2.
 *
 * FLOW EXPLANATION:
 * 1. User clicks "Login with Google" → redirects to Google's login page
 * 2. User logs in and grants permission → Google redirects back with authorization code
 * 3. Spring exchanges the code for ID token and access token (automatic, behind the scenes)
 * 4. Spring calls THIS SERVICE, passing the OAuth2UserRequest (which contains the tokens)
 * 5. WE extract user info (email, name, providerId) from the tokens
 * 6. WE check if the email already exists in our database
 * 7. If EXISTS: Throw error asking user to login with password
 * 8. If NOT EXISTS: Create a new user record with GOOGLE provider
 * 9. Return the user info to Spring, which continues the auth flow
 *
 * KEY RESPONSIBILITY:
 * - Extract user info from Google's OAuth2User response
 * - Handle the duplicate email scenario with helpful error message
 * - Create new user in database with GOOGLE provider
 * - Return OAuth2User object for Spring to continue authentication
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
    private final UserRepository userRepository;

    /**
     * MAIN ENTRY POINT: Called by Spring Security after successful OAuth2 authorization from Google.
     *
     * This method is automatically invoked by Spring Security when:
     * 1. User completes Google login
     * 2. Spring receives the authorization code from Google
     * 3. Spring exchanges code for tokens (ID token + access token)
     *
     * RESPONSIBILITIES:
     * 1. Get user info from Google (name, email, picture, sub)
     * 2. Extract the essential attributes we need
     * 3. Check if user already exists in our database
     * 4. If EXISTS → throw exception (user should login with password)
     * 5. If NOT EXISTS → create new user with GOOGLE provider
     * 6. Return OAuth2User for Spring to continue authentication flow
     *
     * @param userRequest Contains OAuth2 tokens and provider configuration
     * @return OAuth2User object with user attributes from Google
     * @throws OAuth2AuthenticationException if email already exists with different provider
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        log.info("===== CustomOAuth2UserService.loadUser() CALLED =====");
        // Step 1: Call parent service to fetch user information from Google
        // This makes a request to Google's userinfo endpoint using the access token
        // Returns OAuth2User with attributes: email, name, picture, sub, etc.
        try{
            log.info("Calling parent service to load user from Google...");
            OAuth2User oAuth2User=super.loadUser(userRequest);
            log.info("OAuth2 user loaded from Google, extracting attributes...");

            // Step 2: Extract required user information from Google's response
            String email=extractEmail(oAuth2User);
            String fullName=extractFullName(oAuth2User);
            String providerId = extractProviderId(oAuth2User);

            log.info("Processing OAuth2 user: email={}, name={}", email, fullName);

            //Step 3: Check if email already exists in our database
            if(userRepository.existsByEmail(email)){
                log.warn("OAuth2 registration attempted with existing email: {}", email);

                // Email exists - check what provider they used to register
                User existingUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
                    
                log.info("Email exists: email={}, provider={}", email, existingUser.getProvider());
    
                // CASE 1: User registered with email/password (LOCAL provider)
                if (existingUser.getProvider() == AuthProvider.LOCAL) {
                    log.warn("Google OAuth attempted with LOCAL email: {}", email);
                    throw new RuntimeException("You already have an account with this email. Please try logging in with your password.");
                }
    
                // CASE 2: User already registered with Google (GOOGLE provider)
                if (existingUser.getProvider() == AuthProvider.GOOGLE) {
                    log.info("Existing Google user attempting to login: {}", email);
                    // Allow login - return the OAuth2User
                    return oAuth2User;
                }
                
            }

            //Step 4: Email does not exist, so create a neew user with Google provider
            User user=User.builder()
                                    .fullName(fullName)
                                    .email(email.toLowerCase())
                                    .provider(AuthProvider.GOOGLE)
                                    .providerId(providerId)
                                    .password(null)
                                    .enabled(true)
                                    .build();

           userRepository.save(user); 
log.info("New Google OAuth2 user created and flushed: email={}, id={}", user.getEmail(), user.getId());
            log.info("New Google OAuth2 user created: email={}, id={}", user.getEmail(), user.getId());

             // Step 5: Return the OAuth2User object
            // Spring Security uses this to complete the authentication flow
            // The user is now considered authenticated by Google
            return oAuth2User;
        }

        catch(RuntimeException e){
            throw e;
        }
        catch(Exception e){
              log.error("Unexpected error loading OAuth2 user: {}", e.getMessage(), e);
              throw new OAuth2AuthenticationException("Failed to authenticate with Google: " + e.getMessage());
        }

    }

    /**
     * HELPER FUNCTION: Extracts email address from Google's OAuth2User response.
     *
     * Google provides the user's email in the "email" claim of the ID token.
     * This is one of the standard OIDC claims we requested in application.yaml.
     *
     * WHY EMAIL IS CRITICAL:
     * - Email is our primary identifier in the database (unique constraint)
     * - We use email to check if user already exists
     * - We use email to look up the user for future logins
     *
     * @param oAuth2User the OAuth2User object containing Google's user attributes
     * @return the user's email address (lowercase for consistency)
     * @throws RuntimeException if email is not provided by Google
     */

    private String extractEmail(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");

        if (email == null || email.trim().isEmpty()) {
            log.error("Email attribute missing from Google OAuth2 response");
            throw new RuntimeException("Email not provided by Google. Please ensure email scope is requested.");
        }

        return email.toLowerCase();
    }

     /**
     * HELPER FUNCTION: Extracts user's full name from Google's OAuth2User response.
     *
     * Google provides the user's full name in the "name" claim of the ID token.
     * This is a standard OIDC claim we requested in application.yaml.
     *
     * FALLBACK BEHAVIOR:
     * - If Google provides a name → use it
     * - If Google doesn't provide a name → use their email as fallback
     * - This ensures fullName is never null
     *
     * WHY WE DO THIS:
     * - Some users might not have a name set in their Google account
     * - We need to display something in the UI
     * - Email is always available, so it's a safe fallback
     *
     * @param oAuth2User the OAuth2User object containing Google's user attributes
     * @return the user's full name or their email as fallback
     */
    private String extractFullName(OAuth2User oAuth2User){
        String fullName=oAuth2User.getAttribute("name");

        if(fullName != null && !fullName.trim().isEmpty()){
            return fullName;
        }

       log.debug("Full name not provided by Google, using email as fallback");
       return oAuth2User.getAttribute("email");
    }


    /**
     * HELPER FUNCTION: Extracts Google's unique user ID from the OAuth2User response.
     *
     * The "sub" (subject) claim is Google's immutable, unique identifier for each user.
     * Example: "110169415927437948459"
     *
     * WHY THIS IS IMPORTANT:
     * - The "sub" claim is guaranteed to be unique to the user
     * - The "sub" claim never changes, even if user changes their email or name
     * - Prevents account takeover if someone steals the user's email
     * - We store this to link the user permanently to their Google account
     *
     * OIDC GUARANTEE:
     * - All OIDC providers (Google, Facebook, Microsoft, etc.) provide "sub"
     * - It's the standard way to uniquely identify OAuth users
     *
     * @param oAuth2User the OAuth2User object containing Google's user attributes
     * @return Google's unique user ID (the "sub" claim)
     * @throws RuntimeException if sub claim is not found in the response
     */
    private String extractProviderId(OAuth2User oAuth2User) {
        String providerId = oAuth2User.getAttribute("sub");

        if (providerId == null || providerId.trim().isEmpty()) {
            log.error("Provider ID (sub claim) missing from Google OAuth2 response");
            throw new RuntimeException("Google provider ID (sub) not found. OAuth tokens may be malformed.");
        }

        return providerId;
    }



    
}
