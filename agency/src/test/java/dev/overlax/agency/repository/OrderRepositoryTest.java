package dev.overlax.agency.repository;

import dev.overlax.agency.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql("/sql/orders.sql")
class OrderRepositoryTest {

    private static final UUID ANN = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001");

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void givenUserWithOrders_whenFindByUserId_thenReturnsOrdersNewestFirst() {
        List<Order> orders = orderRepository.findByUser_IdOrderByCreatedAtDesc(ANN);

        assertThat(orders).hasSize(2);

        // newest (2026-02-05, total 200) before older (2026-02-01, total 100)
        assertThat(orders.get(0).getTotalPrice()).isEqualByComparingTo("200.00");
        assertThat(orders.get(1).getTotalPrice()).isEqualByComparingTo("100.00");
        assertThat(orders).extracting(o -> o.getUser().getId()).containsOnly(ANN);
    }

    @Test
    void givenUserWithoutOrders_whenFindByUserId_thenReturnsEmpty() {
        assertThat(orderRepository.findByUser_IdOrderByCreatedAtDesc(UUID.randomUUID())).isEmpty();
    }
}
