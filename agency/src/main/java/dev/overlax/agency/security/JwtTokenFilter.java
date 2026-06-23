package dev.overlax.agency.security;

import dev.overlax.agency.security.dto.JwtResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.time.Duration;

public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String ACCESS_TOKEN_COOKIE = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final HandlerExceptionResolver resolver;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider,
                          JwtProperties jwtProperties,
                          HandlerExceptionResolver resolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String accessToken = resolveCookie(request, ACCESS_TOKEN_COOKIE);
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                authenticate(accessToken);
            } else {
                refreshIfPossible(request, response);
            }
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void refreshIfPossible(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = resolveCookie(request, REFRESH_TOKEN_COOKIE);
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return;
        }

        JwtResponse tokens = jwtTokenProvider.refreshUserTokens(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE,
                CookieUtil.access(tokens.accessToken(), Duration.ofHours(jwtProperties.getAccess())).toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                CookieUtil.refresh(tokens.refreshToken(), Duration.ofDays(jwtProperties.getRefresh())).toString());
        authenticate(tokens.accessToken());
    }

    private void authenticate(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String resolveCookie(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }
}
