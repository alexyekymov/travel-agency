package dev.overlax.agency.controller;

import dev.overlax.agency.security.AuthUser;
import dev.overlax.agency.service.OrderService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private static final UUID USER_ID = UUID.randomUUID();

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService))
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
    void givenAuthenticatedUser_whenGetOrders_thenRendersOrders() throws Exception {
        when(orderService.getOrders(USER_ID)).thenReturn(List.of());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/orders"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void givenNonEmptyCart_whenCheckout_thenRedirectsToOrders() throws Exception {
        mockMvc.perform(post("/orders/checkout"))
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).checkout(USER_ID);
    }

    @Test
    void givenEmptyCart_whenCheckout_thenRedirectsToCartWithError() throws Exception {
        when(orderService.checkout(USER_ID)).thenThrow(new IllegalStateException("Cart is empty"));

        mockMvc.perform(post("/orders/checkout"))
                .andExpect(redirectedUrl("/cart"))
                .andExpect(flash().attribute("error", "Cart is empty"));
    }
}
