package dev.overlax.agency.dto;

import dev.overlax.agency.model.type.Role;

import java.util.Set;
import java.util.UUID;

public record UserDTO(UUID id, String email, String firstName, String lastName, Set<Role> roles, boolean active) {

}
