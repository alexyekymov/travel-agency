package dev.overlax.agency.model;

import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tour")
@Getter
@Setter
@NoArgsConstructor
public class Tour {

    @Id
    @Column(name = "id")
    @Generated(event = EventType.INSERT)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "eviction_date")
    private LocalDate evictionDate;

    @Column(name = "tour_type")
    @Enumerated(EnumType.STRING)
    private TourType tourType;

    @Column(name = "hotel_type")
    @Enumerated(EnumType.STRING)
    private HotelType hotelType;

    @Column(name = "transfer_type")
    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Column(name = "hot")
    private Boolean hot;

    @Column(name = "created_at")
    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
