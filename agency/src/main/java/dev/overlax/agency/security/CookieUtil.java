package dev.overlax.agency.security;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public final class CookieUtil {

    public static final String REFRESH_PATH = "/api/auth/refresh";
    private static final String ACCESS_PATH = "/";

    private CookieUtil() {
    }

    public static ResponseCookie access(String value, Duration maxAge) {
        return createCookie(JwtTokenFilter.ACCESS_TOKEN_COOKIE, value, ACCESS_PATH, maxAge);
    }

    public static ResponseCookie refresh(String value, Duration maxAge) {
        return createCookie(JwtTokenFilter.REFRESH_TOKEN_COOKIE, value, REFRESH_PATH, maxAge);
    }

    public static ResponseCookie expiredAccess() {
        return createCookie(JwtTokenFilter.ACCESS_TOKEN_COOKIE, "", ACCESS_PATH, Duration.ZERO);
    }

    public static ResponseCookie expiredRefresh() {
        return createCookie(JwtTokenFilter.REFRESH_TOKEN_COOKIE, "", REFRESH_PATH, Duration.ZERO);
    }

    private static ResponseCookie createCookie(String name, String value, String path, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path(path)
                .maxAge(maxAge)
                .build();
    }
}
