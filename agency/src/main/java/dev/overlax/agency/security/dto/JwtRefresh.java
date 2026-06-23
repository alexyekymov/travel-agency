package dev.overlax.agency.security.dto;

public record JwtRefresh(String refreshToken) {
    public JwtRefresh {
        if (refreshToken.isBlank()) {
            throw new IllegalStateException("Jwt refresh con not be null");
        }
    }
}
