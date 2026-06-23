package dev.overlax.agency.service;

import dev.overlax.agency.dto.RegisterRequest;
import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.model.type.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    UserDTO register(RegisterRequest request);

    UserDTO getUserByEmail(String email);

    UserDTO getById(UUID id);

    List<UserDTO> getAllUsers();

    void blockUser(UUID id);

    void unblockUser(UUID id);

    void changeRoles(UUID id, Set<Role> roles);
}
