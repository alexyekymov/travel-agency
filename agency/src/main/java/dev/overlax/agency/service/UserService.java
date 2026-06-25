package dev.overlax.agency.service;

import dev.overlax.agency.dto.UserRequest;
import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.model.type.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

public interface UserService {

    UserDTO register(UserRequest request);

    UserDTO getUserByEmail(String email);

    UserDTO getById(UUID id);

    Page<UserDTO> getAllUsers(Pageable pageable);

    void blockUser(UUID self, UUID id);

    void unblockUser(UUID self, UUID id);

    void changeRole(UUID self, UUID target, Role role);
}
