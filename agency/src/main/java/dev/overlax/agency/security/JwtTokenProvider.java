package dev.overlax.agency.security;

import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.model.type.Role;
import dev.overlax.agency.security.dto.JwtResponse;
import dev.overlax.agency.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(UUID userId, String username, Set<Role> roles) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId.toString())
                .add("roles", resolveRoles(roles))
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.HOURS);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(Date.from(validity))
                .header().type("jwt").and()
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(UUID userId, String username) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId.toString())
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Refresh token is not valid");
        }

        UUID userId = getId(refreshToken);
        UserDTO user = userService.getById(userId);
        String newAccessToken = createAccessToken(userId, user.email(), user.roles());
        String newRefreshToken = createRefreshToken(userId, user.email());

        return JwtResponse.builder()
                .id(userId)
                .email(user.email())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public boolean validateToken(String token) {
        try {
            return parse(token).getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private UUID getId(String token) {
        return UUID.fromString(parse(token).get("id", String.class));
    }

    private String getUsername(String token) {
        return parse(token).getSubject();
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .toList();
    }
}
