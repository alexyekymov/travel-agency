package dev.overlax.agency.service;

import dev.overlax.agency.dto.RegisterRequest;
import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.exception.EmailAlreadyExistsException;
import dev.overlax.agency.mapper.UserToDtoMapper;
import dev.overlax.agency.model.User;
import dev.overlax.agency.model.type.Role;
import dev.overlax.agency.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.UUID;

@Service
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

        return mapper.toDto(repository.save(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
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

}
