package dev.overlax.agency.service;

import dev.overlax.agency.dto.RegisterRequest;
import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.exception.EmailAlreadyExistsException;
import dev.overlax.agency.mapper.UserToDtoMapper;
import dev.overlax.agency.model.User;
import dev.overlax.agency.model.type.Role;
import dev.overlax.agency.repository.UserRepository;
import dev.overlax.agency.security.AuthUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public UserDTO register(RegisterRequest request) {
        if (repository.existsByEmailIgnoreCase(request.email())) {
            log.warn("Registration rejected, email already in use: {}", request.email());
            throw new EmailAlreadyExistsException(
                    String.format("User with email: %s already exists", request.email()));
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhoneNumber(request.phoneNumber());
        user.setRoles(EnumSet.of(Role.USER));
        user.setActive(true);

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

//    @Override
//    @Transactional
//    public UserDTO changeAccountStatus(UserDTO userDTO) {
//        User dummy = mapper.toUser(userDTO);
//        return repository.findById(dummy.getId())
//                .map(found -> {
//                    found.setActive(dummy.isActive());
//                    repository.save(found);
//                    return mapper.toUserDTO(found);
//                }).orElseThrow(
//                        () -> new EntityNotFoundException(String.format("User with id: %s not found", dummy.getId())));
//
//    }

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
    public void blockUser(UUID id) {
        if (id.equals(currentUserId())) {
            log.warn("Self-block attempt rejected for user {}", id);
            throw new IllegalStateException("You cannot block your own account");
        }
        find(id).setActive(false);
        log.info("User blocked: {}", id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void unblockUser(UUID id) {
        find(id).setActive(true);
        log.info("User unblocked: {}", id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void changeRoles(UUID id, Set<Role> roles) {
        if (id.equals(currentUserId())) {
            log.warn("Self role-change attempt rejected for user {}", id);
            throw new IllegalStateException("You cannot change your own roles");
        }
        find(id).setRoles(roles.isEmpty() ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles));
        log.info("Roles changed for user {}: {}", id, roles);
    }

    private UUID currentUserId() {
        return ((AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    private User find(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id: %s not found", id)));
    }

}
