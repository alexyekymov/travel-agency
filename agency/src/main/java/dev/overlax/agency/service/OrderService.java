package dev.overlax.agency.service;

import dev.overlax.agency.model.Cart;
import dev.overlax.agency.model.CartItem;
import dev.overlax.agency.model.Order;
import dev.overlax.agency.model.OrderItem;
import dev.overlax.agency.model.type.OrderStatus;
import dev.overlax.agency.repository.CartRepository;
import dev.overlax.agency.repository.OrderRepository;
import dev.overlax.agency.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public Order checkout(UUID userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .filter(c -> !c.getItems().isEmpty())
                .orElseThrow(() -> new IllegalStateException("Cart is empty"));

        Order order = new Order();
        order.setUser(userRepository.getReferenceById(userId));
        order.setStatus(OrderStatus.PENDING);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setTour(cartItem.getTour());
            orderItem.setReservedSeats(cartItem.getReservedSeats());
            orderItem.setUnitPrice(cartItem.getTour().getPrice());
            order.addItem(orderItem);
        }

        order.setTotalPrice(order.getItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Order saved = orderRepository.save(order);
        cart.getItems().clear();
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Order> getOrders(UUID userId) {
        return orderRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }
}
