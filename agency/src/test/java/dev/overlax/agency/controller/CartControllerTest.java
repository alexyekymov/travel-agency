package dev.overlax.agency.controller;

import dev.overlax.agency.model.Cart;
import dev.overlax.agency.security.AuthUser;
import dev.overlax.agency.service.CartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private static final UUID USER_ID = UUID.randomUUID();

    @Mock
    private CartService cartService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CartController(cartService))
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();

        AuthUser principal = AuthUser.builder()
                .id(USER_ID).email("cara@example.com").password("x").enabled(true).authorities(List.of())
                .build();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, List.of()));
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenAuthenticatedUser_whenGetCart_thenRendersCart() throws Exception {
        when(cartService.getCart(USER_ID)).thenReturn(new Cart());

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cart"))
                .andExpect(model().attributeExists("cart"));
    }

    @Test
    void givenTourAndSeats_whenAdd_thenAddsToCartAndRedirects() throws Exception {
        UUID tourId = UUID.randomUUID();

        mockMvc.perform(post("/cart/add")
                        .param("tourId", tourId.toString())
                        .param("seats", "3"))
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).addToCart(USER_ID, tourId, 3);
    }

    @Test
    void givenSeats_whenUpdate_thenUpdatesSeatsAndRedirects() throws Exception {
        UUID tourId = UUID.randomUUID();

        mockMvc.perform(post("/cart/update")
                        .param("tourId", tourId.toString())
                        .param("seats", "5"))
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).updateSeats(USER_ID, tourId, 5);
    }

    @Test
    void givenTour_whenRemove_thenRemovesFromCartAndRedirects() throws Exception {
        UUID tourId = UUID.randomUUID();

        mockMvc.perform(post("/cart/remove")
                        .param("tourId", tourId.toString()))
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).removeFromCart(USER_ID, tourId);
    }
}
