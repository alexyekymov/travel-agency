package dev.overlax.agency.repository;

import dev.overlax.agency.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUser_IdOrderByCreatedAtDesc(UUID userId);
}
