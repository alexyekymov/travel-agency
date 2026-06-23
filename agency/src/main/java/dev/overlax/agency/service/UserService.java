package dev.overlax.agency.service;

import dev.overlax.agency.dto.RegisterRequest;
import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.model.type.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

public interface UserService {

    UserDTO register(RegisterRequest request);

    UserDTO getUserByEmail(String email);

    UserDTO getById(UUID id);

    Page<UserDTO> getAllUsers(Pageable pageable);

    void blockUser(UUID id);

    void unblockUser(UUID id);

    void changeRoles(UUID id, Set<Role> roles);
}
