package dev.overlax.agency.controller;

import dev.overlax.agency.dto.UserRequest;
import dev.overlax.agency.exception.EmailAlreadyExistsException;
import dev.overlax.agency.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
class AuthControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService)).build();
    }

    @Test
    void givenSignInRequest_whenGet_thenRendersSignInView() throws Exception {
        mockMvc.perform(get("/auth/sign-in"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/sign-in"));
    }

    @Test
    void givenSignUpRequest_whenGet_thenRendersSignUpForm() throws Exception {
        mockMvc.perform(get("/auth/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/sign-up"))
                .andExpect(model().attributeExists("userRequest"));
    }

    @Test
    void givenValidRegistration_whenPost_thenRegistersAndRedirects() throws Exception {
        mockMvc.perform(post("/auth/sign-up")
                        .param("firstName", "Olena")
                        .param("lastName", "Shevchenko")
                        .param("email", "olena@example.com")
                        .param("password", "Str0ng@Pass")
                        .param("phoneNumber", "+380501234567"))
                .andExpect(redirectedUrl("/auth/sign-in?registered"));

        verify(userService).register(any(UserRequest.class));
    }

    @Test
    void givenInvalidRegistration_whenPost_thenReturnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/auth/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/sign-up"))
                .andExpect(model().attributeHasErrors("userRequest"));

        verify(userService, never()).register(any());
    }

    @Test
    void givenTakenEmail_whenPost_thenReturnsFormWithEmailError() throws Exception {
        when(userService.register(any(UserRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("taken"));

        mockMvc.perform(post("/auth/sign-up")
                        .param("firstName", "Olena")
                        .param("lastName", "Shevchenko")
                        .param("email", "olena@example.com")
                        .param("password", "Str0ng@Pass"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/sign-up"))
                .andExpect(model().attributeHasFieldErrors("userRequest", "email"));
    }
}
