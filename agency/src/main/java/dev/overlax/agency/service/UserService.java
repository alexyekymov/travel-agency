package dev.overlax.agency.service;

import dev.overlax.agency.dto.UserDTO;

import java.util.UUID;

public interface UserService {
//    UserDTO register(UserDTO userDTO);
//
//    UserDTO updateUser(String username, UserDTO userDTO);
//
    UserDTO getUserByEmail(String email);
//
//    UserDTO changeAccountStatus(UserDTO userDTO);
//
    UserDTO getById(UUID id);
}
