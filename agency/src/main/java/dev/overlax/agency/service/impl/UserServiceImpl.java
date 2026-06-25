package dev.overlax.agency.service.impl;

import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.dto.UserRequest;
import dev.overlax.agency.exception.EmailAlreadyExistsException;
import dev.overlax.agency.mapper.UserToDtoMapper;
import dev.overlax.agency.model.User;
import dev.overlax.agency.model.type.Role;
import dev.overlax.agency.repository.UserRepository;
import dev.overlax.agency.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserToDtoMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO register(UserRequest request) {
        if (repository.existsByEmailIgnoreCase(request.email())) {
            log.warn("Registration rejected, email already in use: {}", request.email());
            throw new EmailAlreadyExistsException(
                    String.format("User with email: %s already exists", request.email()));
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .roles(EnumSet.of(Role.USER))
                .active(true)
                .build();


        User saved = repository.save(user);
        log.info("New user registered: id={}, email={}", saved.getId(), saved.getEmail());
        return mapper.toDto(saved);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.debug("Looking up user by email: {}", email);
        return repository.findByEmailIgnoreCase(email)
                .map(mapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("User with username: %s not found", email)));
    }

    @Override
    public UserDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id: %s not found", id)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void blockUser(UUID self, UUID target) {
        if (self.equals(target)) {
            log.warn("Self-block attempt rejected for user {}", self);
            throw new IllegalStateException("You cannot block your own account");
        }
        find(target).setActive(false);
        log.info("User blocked: {}", target);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void unblockUser(UUID self, UUID target) {
        if (self.equals(target)) {
            log.warn("Self-unblock attempt rejected for user {}", self);
            throw new IllegalStateException("You cannot unblock your own account");
        }
        find(target).setActive(true);
        log.info("User unblocked: {}", target);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void changeRole(UUID self, UUID target, Role role) {
        if (self.equals(target)) {
            log.warn("Self role-change attempt rejected for user {}", self);
            throw new IllegalStateException("You cannot change your own roles");
        }
        if (role == null) {
            log.warn("Empty role-change attempt rejected for user {}", target);
            throw new IllegalStateException("A user must have at least one role");
        }
        Set<Role> roles = withHierarchy(role);
        find(target).setRoles(roles);
        log.info("Roles changed for user {}: {}", target, roles);
    }

    private static Set<Role> withHierarchy(Role role) {
        EnumSet<Role> result = EnumSet.of(role);
        if (result.contains(Role.ADMIN)) {
            result.add(Role.MANAGER);
        }
        if (result.contains(Role.MANAGER)) {
            result.add(Role.USER);
        }
        return result;
    }

    private User find(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id: %s not found", id)));
    }

}
