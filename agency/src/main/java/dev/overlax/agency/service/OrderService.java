package dev.overlax.agency.service;

import dev.overlax.agency.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order checkout(UUID userId);

    List<Order> getOrders(UUID userId);
}
