package dev.overlax.agency.service.impl;

import dev.overlax.agency.model.Cart;
import dev.overlax.agency.model.CartItem;
import dev.overlax.agency.model.Order;
import dev.overlax.agency.model.OrderItem;
import dev.overlax.agency.model.type.OrderStatus;
import dev.overlax.agency.service.OrderService;
import dev.overlax.agency.repository.CartRepository;
import dev.overlax.agency.repository.OrderRepository;
import dev.overlax.agency.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Order checkout(UUID userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .filter(c -> !c.getItems().isEmpty())
                .orElseThrow(() -> {
                    log.warn("Checkout rejected, empty cart for user {}", userId);
                    return new IllegalStateException("Cart is empty");
                });

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
        log.info("Order placed: id={}, user={}, total={}", saved.getId(), userId, saved.getTotalPrice());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrders(UUID userId) {
        log.debug("Fetching orders for user {}", userId);
        return orderRepository.findByUser_IdOrderByCreatedAtDesc(userId);
    }
}
