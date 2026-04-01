package com.app.quantitymeasurement.auth.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.app.quantitymeasurement.auth.entity.RefreshToken;
import com.app.quantitymeasurement.auth.entity.User;
import com.app.quantitymeasurement.auth.repository.UserRepository;
import com.app.quantitymeasurement.auth.service.JwtService;
import com.app.quantitymeasurement.auth.service.RefreshTokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2LoginSuccessHandler.java
 *
 * PURPOSE: Handle what happens AFTER successful Google OAuth2 authentication.
 *
 * FLOW EXPLANATION:
 * 1. User clicks "Login with Google"
 * 2. Spring redirects to Google → user logs in
 * 3. Google redirects back to us with authorization code
 * 4. Spring exchanges code for ID token + access token (automatic)
 * 5. Spring calls CustomOAuth2UserService.loadUser() → creates/validates user in database
 * 6. Spring calls THIS HANDLER → we ARE HERE NOW
 * 7. We generate JWT tokens (access + refresh)
 * 8. We redirect frontend with tokens in URL
 * 9. Frontend receives URL, extracts tokens, stores them
 * 10. Frontend uses tokens for all future API calls
 *
 * KEY CONCEPT: We convert Google's authentication into OUR JWT tokens.
 * Why? Because our API expects JWT tokens, not Google tokens.
 *
 * FLOW VISUAL:
 * Google OAuth → User in DB → JWT Tokens → Redirect with URL → Frontend uses JWT
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
    
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    /**
     * MAIN ENTRY POINT: Called by Spring after successful Google OAuth2 authentication.
     *
     * AT THIS MOMENT:
     * - User has been authenticated by Google ✓
     * - User has been created/validated in database ✓
     * - Spring has created an Authentication object ✓
     * - We now need to generate JWT tokens and send them to frontend ✓
     *
     * STEPS THIS METHOD PERFORMS:
     * 1. Extract the OAuth2User principal from Authentication
     * 2. Get the email from OAuth2User
     * 3. Fetch the User entity from database using email
     * 4. Generate JWT access token (15 minute validity)
     * 5. Generate JWT refresh token (7 day validity)
     * 6. Build redirect URL with both tokens
     * 7. Redirect browser to that URL
     *
     * @param request the HTTP request (contains original request data)
     * @param response the HTTP response (used to send redirect)
     * @param authentication Spring's Authentication object (contains OAuth2User)
     *
     * @throws IOException if network/redirect fails
     * @throws ServletException if servlet operation fails
     */

    @Override
    public void onAuthenticationSuccess(


        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException{

        try{
            // STEP 1: Extract OAuth2User from Authentication
            // authentication.getPrincipal() returns the authenticated user
            // In OAuth2 context, it's an OAuth2User object
            // We cast it to OAuth2User to access its methods

            OAuth2User oAuth2User=(OAuth2User) authentication.getPrincipal();
            String email=oAuth2User.getAttribute("email");

             log.info("OAuth2 authentication successful for: {}", email);

            // STEP 2: Fetch User entity from database
            // CustomOAuth2UserService already created this user
            // We need the full User entity (not just OAuth2User) to generate JWT
            // Why? Because JwtService.generateAccessToken() expects a User object
            User user = userRepository.findByEmail(email.toLowerCase())
                    .orElseThrow(() -> new RuntimeException("User not found after OAuth2 authentication"));

            log.debug("User found in database: id={}, email={}, provider={}", 
                    user.getId(), user.getEmail(), user.getProvider());

            // STEP 3: Generate JWT access token
            // This token is valid for 15 minutes (as configured in application.yaml)
            // Frontend will include this in Authorization header: "Bearer {accessToken}"
            // Used for: Making API calls to /api/quantities, /api/measurements, etc.
            String accessToken=jwtService.generateAccessToken(user);

             log.debug("Access token generated for user: {}", email);


             // STEP 4: Generate JWT refresh token
            // This token is valid for 7 days (as configured in application.yaml)
            // Frontend will store this separately
            // Used for: When access token expires, frontend sends refresh token to get new access token

            RefreshToken refreshToken=refreshTokenService.createRefreshToken(user);

             // STEP 5: Build the redirect URL with tokens
            // Frontend is waiting at http://localhost:3000/oauth/callback
            // We send the URL: http://localhost:3000/oauth/callback?accessToken=...&refreshToken=...&email=...
            
            String  redirectURI=buildRedirectUrl(accessToken,refreshToken.getToken(),email);

            
            log.info("Redirecting OAuth2 user to frontend with tokens");

            // STEP 6: Send the redirect response
            // Browser will navigate to the redirectUrl
            // Frontend will parse the URL and extract tokens

            getRedirectStrategy().sendRedirect(request, response, redirectURI);

        }
        catch(Exception e){
            log.error("Error during OAuth2 success handling: {}", e.getMessage(), e);
            throw new ServletException("Failed to handle OAuth2 success: " + e.getMessage());
        }
    }



    //Helper function to build url
        private String buildRedirectUrl(String accessToken, String refreshToken, String email){
             // Redirect to React app OAuth callback handler
            String baseUrl = "http://localhost:5175/oauth-callback";

            //Encodig the url
            String encodedAccessToken = URLEncoder.encode(accessToken,StandardCharsets.UTF_8);
            String encodedRefreshToken=URLEncoder.encode(refreshToken,StandardCharsets.UTF_8);

            String redirectURI=String.format(
              "%s?accessToken=%s&refreshToken=%s&email=%s",
              baseUrl,
              encodedAccessToken,
              encodedRefreshToken,
              email  
            );
            
             log.debug("Redirect URL built: {}", baseUrl + "?accessToken=***&refreshToken=***&email=" + email);

             return redirectURI;
        }
        
}
