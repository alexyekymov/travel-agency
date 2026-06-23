package dev.overlax.agency.security;

import dev.overlax.agency.model.User;

import java.util.Set;

public class AuthUserFactory {

    public static AuthUser create(User user) {
        return AuthUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(Set.copyOf(user.getRoles()))
                .build();
    }
}
