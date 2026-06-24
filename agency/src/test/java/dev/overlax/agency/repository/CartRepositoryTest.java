package dev.overlax.agency.repository;

import dev.overlax.agency.model.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/sql/cart.sql")
class CartRepositoryTest {

    private static final UUID CARA = UUID.fromString("cccccccc-0000-0000-0000-000000000001");

    @Autowired
    private CartRepository cartRepository;

    @Test
    void givenUserWithCart_whenFindByUserId_thenReturnsCart() {
        Optional<Cart> cart = cartRepository.findByUser_Id(CARA);

        assertThat(cart).isPresent();
        assertThat(cart.get().getUser().getId()).isEqualTo(CARA);
    }

    @Test
    void givenUserWithoutCart_whenFindByUserId_thenReturnsEmpty() {
        assertThat(cartRepository.findByUser_Id(UUID.randomUUID())).isEmpty();
    }
}
