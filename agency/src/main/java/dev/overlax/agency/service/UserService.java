package dev.overlax.agency.service;

import dev.overlax.agency.dto.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    UserDTO updateUser(String username, UserDTO userDTO);

    UserDTO getUserByUsername(String username);

    UserDTO changeAccountStatus(UserDTO userDTO);

    UserDTO getUserById(UUID id);
}
