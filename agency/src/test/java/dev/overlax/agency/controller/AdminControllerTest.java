package dev.overlax.agency.controller;

import dev.overlax.agency.model.type.Role;
import dev.overlax.agency.security.AuthUser;
import dev.overlax.agency.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private UUID adminId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminController(userService))
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver(),
                        new AuthenticationPrincipalArgumentResolver())
                .build();

        adminId = UUID.randomUUID();
        AuthUser principal = AuthUser.builder()
                .id(adminId)
                .email("admin@example.com")
                .password("x")
                .enabled(true)
                .authorities(List.of())
                .build();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, List.of()));
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenRequest_whenUsers_thenRendersUserList() throws Exception {
        when(userService.getAllUsers(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void givenUserId_whenBlock_thenBlocksAndRedirects() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/admin/users/{id}/block", id))
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService).blockUser(adminId, id);
    }

    @Test
    void givenUserId_whenUnblock_thenUnblocksAndRedirects() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/admin/users/{id}/unblock", id))
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService).unblockUser(adminId, id);
    }

    @Test
    void givenRole_whenChangeRoles_thenUpdatesRoleAndRedirects() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/admin/users/{id}/role", id)
                        .param("role", "ADMIN"))
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService).changeRole(adminId, id, Role.ADMIN);
    }

    @Test
    void givenNoRole_whenChangeRoles_thenBadRequestAndServiceNotCalled() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/admin/users/{id}/role", id))
                .andExpect(status().isBadRequest());

        verify(userService, never()).changeRole(any(), any(), any());
    }
}
