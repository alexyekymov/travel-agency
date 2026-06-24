package dev.overlax.agency.service;

import dev.overlax.agency.model.Cart;
import dev.overlax.agency.model.CartItem;
import dev.overlax.agency.model.Tour;
import dev.overlax.agency.repository.CartRepository;
import dev.overlax.agency.repository.TourRepository;
import dev.overlax.agency.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private TourRepository tourRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService service;

    private CartItem itemFor(UUID tourId, int seats) {
        Tour tour = new Tour();
        tour.setId(tourId);
        CartItem item = new CartItem();
        item.setTour(tour);
        item.setReservedSeats(seats);
        return item;
    }

    @Test
    void givenTourAlreadyInCart_whenAddToCart_thenIncrementsSeats() {
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();
        Cart cart = new Cart();
        CartItem existing = itemFor(tourId, 1);
        cart.addItem(existing);
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(cart));

        service.addToCart(userId, tourId, 2);

        assertThat(existing.getReservedSeats()).isEqualTo(3);
    }

    @Test
    void givenTourNotInCart_whenAddToCart_thenAddsNewItem() {
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();
        Cart cart = new Cart();
        Tour tour = new Tour();
        tour.setId(tourId);
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(cart));
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));

        service.addToCart(userId, tourId, 3);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getTour()).isSameAs(tour);
        assertThat(cart.getItems().get(0).getReservedSeats()).isEqualTo(3);
    }

    @Test
    void givenUnknownTour_whenAddToCart_thenThrows() {
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(new Cart()));
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addToCart(userId, tourId, 1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void givenZeroSeats_whenUpdateSeats_thenRemovesItem() {
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.addItem(itemFor(tourId, 2));
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(cart));

        service.updateSeats(userId, tourId, 0);

        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void givenPositiveSeats_whenUpdateSeats_thenSetsNewCount() {
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();
        Cart cart = new Cart();
        CartItem item = itemFor(tourId, 2);
        cart.addItem(item);
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(cart));

        service.updateSeats(userId, tourId, 5);

        assertThat(item.getReservedSeats()).isEqualTo(5);
    }

    @Test
    void givenItemInCart_whenRemoveFromCart_thenRemovesItem() {
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.addItem(itemFor(tourId, 2));
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(cart));

        service.removeFromCart(userId, tourId);

        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void givenCartWithItems_whenClear_thenEmptiesCart() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.addItem(itemFor(UUID.randomUUID(), 1));
        cart.addItem(itemFor(UUID.randomUUID(), 2));
        when(cartRepository.findByUser_Id(userId)).thenReturn(Optional.of(cart));

        service.clear(userId);

        assertThat(cart.getItems()).isEmpty();
    }
}
