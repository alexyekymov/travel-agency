package dev.overlax.agency.service;

import dev.overlax.agency.model.Cart;
import dev.overlax.agency.model.CartItem;
import dev.overlax.agency.model.Tour;
import dev.overlax.agency.repository.CartRepository;
import dev.overlax.agency.repository.TourRepository;
import dev.overlax.agency.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Cart getCart(UUID userId) {
        return cartRepository.findByUser_Id(userId).orElseGet(Cart::new);
    }

    public void addToCart(UUID userId, UUID tourId, int seats) {
        Cart cart = getOrCreateCart(userId);
        findItem(cart, tourId).ifPresentOrElse(
                item -> item.setReservedSeats(item.getReservedSeats() + seats),
                () -> cart.addItem(newItem(tourId, seats)));
    }

    public void updateSeats(UUID userId, UUID tourId, int seats) {
        Cart cart = getOrCreateCart(userId);
        findItem(cart, tourId).ifPresent(item -> {
            if (seats <= 0) {
                cart.removeItem(item);
            } else {
                item.setReservedSeats(seats);
            }
        });
    }

    public void removeFromCart(UUID userId, UUID tourId) {
        cartRepository.findByUser_Id(userId)
                .ifPresent(cart -> findItem(cart, tourId).ifPresent(cart::removeItem));
    }

    public void clear(UUID userId) {
        cartRepository.findByUser_Id(userId).ifPresent(cart -> cart.getItems().clear());
    }

    private Cart getOrCreateCart(UUID userId) {
        return cartRepository.findByUser_Id(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(userRepository.getReferenceById(userId));
            return cartRepository.save(cart);
        });
    }

    private Optional<CartItem> findItem(Cart cart, UUID tourId) {
        return cart.getItems().stream()
                .filter(item -> item.getTour().getId().equals(tourId))
                .findFirst();
    }

    private CartItem newItem(UUID tourId, int seats) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found: " + tourId));
        CartItem item = new CartItem();
        item.setTour(tour);
        item.setReservedSeats(seats);
        return item;
    }
}
