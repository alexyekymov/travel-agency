package dev.overlax.agency.service;

import dev.overlax.agency.dto.UserRequest;
import dev.overlax.agency.dto.UserDTO;
import dev.overlax.agency.exception.EmailAlreadyExistsException;
import dev.overlax.agency.mapper.UserToDtoMapper;
import dev.overlax.agency.model.User;
import dev.overlax.agency.model.type.Role;
import dev.overlax.agency.repository.UserRepository;
import dev.overlax.agency.security.AuthUser;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserToDtoMapper mapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenNewEmail_whenRegister_thenEncodesPasswordAssignsUserRoleAndReturnsDto() {
        UserRequest request = new UserRequest(
                "Olena", "Shevchenko", "olena@example.com", "Str0ng@Pass", "+380501234567");
        UserDTO expected = new UserDTO(UUID.randomUUID(), "olena@example.com",
                "Olena", "Shevchenko", Set.of(Role.USER), true);

        when(repository.existsByEmailIgnoreCase("olena@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Str0ng@Pass")).thenReturn("ENCODED");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDto(any(User.class))).thenReturn(expected);

        UserDTO result = service.register(request);

        assertThat(result).isSameAs(expected);

        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(repository).save(saved.capture());
        User user = saved.getValue();
        assertThat(user.getFirstName()).isEqualTo("Olena");
        assertThat(user.getLastName()).isEqualTo("Shevchenko");
        assertThat(user.getEmail()).isEqualTo("olena@example.com");
        assertThat(user.getPassword()).isEqualTo("ENCODED");
        assertThat(user.getRoles()).containsExactly(Role.USER);
        assertThat(user.isActive()).isTrue();
    }

    @Test
    void givenExistingEmail_whenRegister_thenThrowsAndDoesNotSave() {
        UserRequest request = new UserRequest(
                "Ihor", "Tkachenko", "ihor@example.com", "Str0ng@Pass", null);
        when(repository.existsByEmailIgnoreCase("ihor@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void givenExistingEmail_whenGetUserByEmail_thenReturnsDto() {
        User user = new User();
        UserDTO expected = new UserDTO(UUID.randomUUID(), "olena@example.com",
                "Olena", "Shevchenko", Set.of(Role.USER), true);
        when(repository.findByEmailIgnoreCase("olena@example.com")).thenReturn(Optional.of(user));
        when(mapper.toDto(user)).thenReturn(expected);

        assertThat(service.getUserByEmail("olena@example.com")).isSameAs(expected);
    }

    @Test
    void givenUnknownEmail_whenGetUserByEmail_thenThrows() {
        when(repository.findByEmailIgnoreCase("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserByEmail("ghost@example.com"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void givenUnknownId_whenGetById_thenThrows() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void givenAnotherUser_whenBlockUser_thenDeactivatesAccount() {
        authenticateAs(UUID.randomUUID());
        UUID targetId = UUID.randomUUID();
        User target = new User();
        target.setActive(true);
        when(repository.findById(targetId)).thenReturn(Optional.of(target));

        service.blockUser(targetId);

        assertThat(target.isActive()).isFalse();
    }

    @Test
    void givenOwnId_whenBlockUser_thenRejects() {
        UUID adminId = UUID.randomUUID();
        authenticateAs(adminId);

        assertThatThrownBy(() -> service.blockUser(adminId))
                .isInstanceOf(IllegalStateException.class);

        verify(repository, never()).findById(any());
    }

    @Test
    void givenBlockedUser_whenUnblockUser_thenReactivatesAccount() {
        UUID targetId = UUID.randomUUID();
        User target = new User();
        target.setActive(false);
        when(repository.findById(targetId)).thenReturn(Optional.of(target));

        service.unblockUser(targetId);

        assertThat(target.isActive()).isTrue();
    }

    @Test
    void givenAnotherUser_whenChangeRoles_thenReplacesRoles() {
        authenticateAs(UUID.randomUUID());
        UUID targetId = UUID.randomUUID();
        User target = new User();
        when(repository.findById(targetId)).thenReturn(Optional.of(target));

        service.changeRoles(targetId, Set.of(Role.MANAGER, Role.ADMIN));

        assertThat(target.getRoles()).containsExactlyInAnyOrder(Role.MANAGER, Role.ADMIN);
    }

    @Test
    void givenOwnId_whenChangeRoles_thenRejects() {
        UUID adminId = UUID.randomUUID();
        authenticateAs(adminId);

        assertThatThrownBy(() -> service.changeRoles(adminId, Set.of(Role.USER)))
                .isInstanceOf(IllegalStateException.class);

        verify(repository, never()).findById(any());
    }

    private void authenticateAs(UUID id) {
        AuthUser principal = AuthUser.builder()
                .id(id)
                .email("admin@example.com")
                .password("x")
                .enabled(true)
                .authorities(List.of())
                .build();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, List.of()));
        SecurityContextHolder.setContext(context);
    }
}
