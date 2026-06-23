package dev.overlax.agency.security;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public final class CookieUtil {

    private static final String PATH = "/";

    private CookieUtil() {
    }

    public static ResponseCookie access(String value, Duration maxAge) {
        return createCookie(JwtTokenFilter.ACCESS_TOKEN_COOKIE, value, maxAge);
    }

    public static ResponseCookie refresh(String value, Duration maxAge) {
        return createCookie(JwtTokenFilter.REFRESH_TOKEN_COOKIE, value, maxAge);
    }

    public static ResponseCookie expiredAccess() {
        return createCookie(JwtTokenFilter.ACCESS_TOKEN_COOKIE, "", Duration.ZERO);
    }

    public static ResponseCookie expiredRefresh() {
        return createCookie(JwtTokenFilter.REFRESH_TOKEN_COOKIE, "", Duration.ZERO);
    }

    private static ResponseCookie createCookie(String name, String value, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path(PATH)
                .maxAge(maxAge)
                .build();
    }
}
