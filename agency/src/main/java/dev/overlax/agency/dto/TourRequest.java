package dev.overlax.agency.dto;

import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TourRequest(
        @NotBlank(message = "{tour.title.notBlank}")
        @Size(max = 150, message = "{tour.title.size}")
        String title,

        String description,

        @NotNull(message = "{tour.price.notNull}")
        @PositiveOrZero(message = "{tour.price.positive}")
        BigDecimal price,

        @NotNull(message = "{tour.arrivalDate.notNull}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate,

        @NotNull(message = "{tour.evictionDate.notNull}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate evictionDate,

        @NotNull(message = "{tour.tourType.notNull}") TourType tourType,
        @NotNull(message = "{tour.hotelType.notNull}") HotelType hotelType,
        @NotNull(message = "{tour.transferType.notNull}") TransferType transferType,
        Boolean hot, MultipartFile image) {
}
