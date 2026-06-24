package dev.overlax.agency.controller;

import dev.overlax.agency.model.type.Role;
import dev.overlax.agency.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminController(userService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
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

        verify(userService).blockUser(id);
    }

    @Test
    void givenUserId_whenUnblock_thenUnblocksAndRedirects() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/admin/users/{id}/unblock", id))
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService).unblockUser(id);
    }

    @Test
    void givenRoles_whenChangeRoles_thenUpdatesRolesAndRedirects() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/admin/users/{id}/roles", id)
                        .param("roles", "ADMIN", "MANAGER"))
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService).changeRoles(id, Set.of(Role.ADMIN, Role.MANAGER));
    }

    @Test
    void givenNoRoles_whenChangeRoles_thenPassesEmptySet() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/admin/users/{id}/roles", id))
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService).changeRoles(id, Set.of());
    }
}
