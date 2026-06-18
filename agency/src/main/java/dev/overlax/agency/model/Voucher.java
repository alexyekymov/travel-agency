package dev.overlax.agency.model;

import dev.overlax.agency.model.type.VoucherStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "voucher")
@Getter
@Setter
@NoArgsConstructor
public class Voucher {

    @Id
    @Column(name = "id")
    @Generated(event = EventType.INSERT)
    private UUID id;

    @Column(nullable = false, unique = true, length = 36)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VoucherStatus status;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "order_item_id", unique = true)
    private OrderItem orderItem;
}
