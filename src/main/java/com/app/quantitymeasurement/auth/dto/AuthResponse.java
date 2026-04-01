package com.app.quantitymeasurement.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private String email;
    private String fullName;
    private String message;

    public static AuthResponse success(String accessToken, String refreshToken, Long expiresIn, String email, String fullName) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .email(email)
                .fullName(fullName)
                .build();
    }

    public static AuthResponse message(String message) {
        return AuthResponse.builder()
                .message(message)
                .build();
    }
}
