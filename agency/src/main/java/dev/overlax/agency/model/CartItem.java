package dev.overlax.agency.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @Column(name = "id")
    @Generated(event = EventType.INSERT)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(name = "reserved_seats")
    private int reservedSeats;

    public BigDecimal getLineTotal() {
        return tour.getPrice().multiply(BigDecimal.valueOf(reservedSeats));
    }
}
