package com.app.quantitymeasurement.auth.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2LoginFailureHandler handles failed Google OAuth2 authentication.
 * Extracts error message and redirects to frontend with error in URL.
 */
@Slf4j
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * Called when OAuth2 authentication fails.
     * Extracts error, logs it, and redirects to frontend with error message.
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        try {
            // Extract error message from exception
            String errorMessage = extractErrorMessage(exception);
            log.warn("OAuth2 authentication failed. Error: {}", errorMessage);

            // Build redirect URL with error
            String redirectUrl = buildErrorRedirectUrl(errorMessage);
            log.info("Redirecting to frontend with error");

            // Redirect to frontend
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("Error during OAuth2 failure handling: {}", e.getMessage(), e);
            throw new ServletException("Failed to handle OAuth2 failure: " + e.getMessage());
        }
    }

    /**
     * Extracts error message from the exception.
     * Returns message from exception, or default if null.
     */
    private String extractErrorMessage(AuthenticationException exception) {
        String message = exception.getMessage();

        // Try cause if message is null
        if (message == null || message.trim().isEmpty()) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                message = cause.getMessage();
            }
        }

        // Default message if still null
        if (message == null || message.trim().isEmpty()) {
            message = "Authentication failed. Please try again.";
        }

        return message;
    }

    /**
     * Builds error redirect URL: http://localhost:3000/oauth/callback?error=...
     * URL encodes error message for safe transmission in URL.
     */
    private String buildErrorRedirectUrl(String errorMessage) {
        // Frontend callback endpoint
        String baseUrl = "http://localhost:8080/oauth-callback.html";

        // URL encode the error message
        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        // Build complete error URL
        String redirectUrl = String.format(
            "%s?error=%s",
            baseUrl,
            encodedError
        );

        log.debug("Error redirect URL built: {}", baseUrl + "?error=***");

        return redirectUrl;
    }
}