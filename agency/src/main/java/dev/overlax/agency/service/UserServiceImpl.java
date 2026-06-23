package dev.overlax.agency.service;

import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.mapper.UserToDtoMapper;
import dev.overlax.agency.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserToDtoMapper mapper;

//    @Override
//    public UserDTO register(UserDTO userDTO) {
//        return null;
//    }
//
//    @Override
//    public UserDTO updateUser(String username, UserDTO userDTO) {
//        return null;
//    }

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
