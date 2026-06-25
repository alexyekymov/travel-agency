package dev.overlax.agency.service;

import dev.overlax.agency.model.Cart;
import dev.overlax.agency.model.CartItem;
import dev.overlax.agency.model.Order;
import dev.overlax.agency.model.Tour;
import dev.overlax.agency.model.User;
import dev.overlax.agency.model.type.OrderStatus;
import dev.overlax.agency.repository.CartRepository;
import dev.overlax.agency.repository.OrderRepository;
import dev.overlax.agency.repository.UserRepository;
import dev.overlax.agency.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl service;

    private CartItem cartItem(String price, int seats) {
        Tour tour = new Tour();
        tour.setPrice(new BigDecimal(price));
        CartItem item = new CartItem();
        item.setTour(tour);
        item.setReservedSeats(seats);
        return item;
    }

    @Test
    void givenCartWithItems_whenCheckout_thenBuildsPendingOrderTotalsAndClearsCart() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.addItem(cartItem("100.00", 2)); // 200
        cart.addItem(cartItem("50.00", 1));  // 50

        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(cart));
        when(userRepository.getReferenceById(userId)).thenReturn(new User());
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = service.checkout(userId);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getItems()).hasSize(2);
        assertThat(order.getTotalPrice()).isEqualByComparingTo("250.00");
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void givenNoCart_whenCheckout_thenThrows() {
        UUID userId = UUID.randomUUID();
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.checkout(userId))
                .isInstanceOf(IllegalStateException.class);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void givenEmptyCart_whenCheckout_thenThrows() {
        UUID userId = UUID.randomUUID();
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(new Cart()));

        assertThatThrownBy(() -> service.checkout(userId))
                .isInstanceOf(IllegalStateException.class);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void givenUserId_whenGetOrders_thenDelegatesToRepository() {
        UUID userId = UUID.randomUUID();
        List<Order> orders = List.of(new Order());
        when(orderRepository.findByUser_IdOrderByCreatedAtDesc(userId)).thenReturn(orders);

        assertThat(service.getOrders(userId)).isSameAs(orders);
    }
}
