package dev.overlax.agency.service;

import dev.overlax.agency.model.Cart;

import java.util.UUID;

public interface CartService {

    Cart getCart(UUID userId);

    void addToCart(UUID userId, UUID tourId, int seats);

    void updateSeats(UUID userId, UUID tourId, int seats);

    void removeFromCart(UUID userId, UUID tourId);

    void clear(UUID userId);
}
