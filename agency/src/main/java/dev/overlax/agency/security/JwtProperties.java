package dev.overlax.agency.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

    private String secret;
    private long access;
    private long refresh;

}
