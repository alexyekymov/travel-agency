package dev.overlax.agency.config;

import dev.overlax.agency.repository.UserRepository;
import dev.overlax.agency.security.JwtProperties;
import dev.overlax.agency.security.JwtTokenFilter;
import dev.overlax.agency.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;
    private final JwtTokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final HandlerExceptionResolver resolver;

    public SecurityConfig(UserRepository userRepository, ApplicationContext applicationContext, JwtTokenProvider tokenProvider, JwtProperties jwtProperties, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.userRepository = userRepository;
        this.applicationContext = applicationContext;
        this.tokenProvider = tokenProvider;
        this.jwtProperties = jwtProperties;
        this.resolver = resolver;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auths ->
                        auths
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/", "/auth/sign-in", "/auth/sign-up", "/api/auth/login").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/auth/sign-in")))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies(JwtTokenFilter.ACCESS_TOKEN_COOKIE, JwtTokenFilter.REFRESH_TOKEN_COOKIE)
                        .logoutSuccessUrl("/auth/sign-in?logout"))
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(tokenProvider, jwtProperties, resolver);
    }
}
