package dev.overlax.agency.restcontroller;

import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.security.CookieUtil;
import dev.overlax.agency.security.JwtProperties;
import dev.overlax.agency.security.JwtTokenFilter;
import dev.overlax.agency.security.JwtTokenProvider;
import dev.overlax.agency.security.dto.JwtRequest;
import dev.overlax.agency.security.dto.JwtResponse;
import dev.overlax.agency.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final UserService userService;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDTO user = userService.getUserByEmail(request.getUsername());
        String accessToken = tokenProvider.createAccessToken(user.id(), user.email(), user.roles());
        String refreshToken = tokenProvider.createRefreshToken(user.id(), user.email());

        setTokenCookies(response, accessToken, refreshToken);

        return JwtResponse.builder()
                .id(user.id())
                .email(user.email())
                .build();
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(
            @CookieValue(name = JwtTokenFilter.REFRESH_TOKEN_COOKIE, required = false) String refreshToken,
            HttpServletResponse response) {
        JwtResponse tokens = tokenProvider.refreshUserTokens(refreshToken);
        setTokenCookies(response, tokens.accessToken(), tokens.refreshToken());

        return JwtResponse.builder()
                .id(tokens.id())
                .email(tokens.email())
                .build();
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, CookieUtil.expiredAccess().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, CookieUtil.expiredRefresh().toString());
    }

    private void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        response.addHeader(HttpHeaders.SET_COOKIE,
                CookieUtil.access(accessToken, Duration.ofHours(jwtProperties.getAccess())).toString());
        response.addHeader(HttpHeaders.SET_COOKIE,
                CookieUtil.refresh(refreshToken, Duration.ofDays(jwtProperties.getRefresh())).toString());
    }
}
