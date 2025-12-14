package com.cemware.lavine.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long userId,
        String email,
        String name
) {
    public static AuthResponse of(String accessToken, String refreshToken, Long userId, String email, String name) {
        return new AuthResponse(accessToken, refreshToken, "Bearer", userId, email, name);
    }
}

