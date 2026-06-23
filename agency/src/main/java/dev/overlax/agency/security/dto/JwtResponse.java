package dev.overlax.agency.security.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JwtResponse(UUID id, String email, String accessToken, String refreshToken) {

}
