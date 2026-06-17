package dev.overlax.agency.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "\"voucher\"")
public class Voucher {

    @Id
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "tourType")
    @Enumerated(EnumType.STRING)
    private TourType tourType;

    @Column(name = "transferType")
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Column(name = "hotelType")
    @Enumerated(EnumType.STRING)
    private HotelType hotelType;

    @Column(name = "status")
    private VoucherStatus status;

    @Column(name = "arrivalDate")
    private LocalDate arrivalDate;

    @Column(name = "evictionDate")
    private LocalDate evictionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_hot")
    private boolean isHot;
}
