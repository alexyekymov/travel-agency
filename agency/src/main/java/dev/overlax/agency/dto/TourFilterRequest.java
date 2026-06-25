package dev.overlax.agency.dto;

import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TourFilterRequest(String title,
                                TourType tourType,
                                TransferType transferType,
                                HotelType hotelType,
                                @PositiveOrZero(message = "{tourFilter.minPrice.positive}")
                                BigDecimal minPrice,
                                @PositiveOrZero(message = "{tourFilter.maxPrice.positive}")
                                BigDecimal maxPrice,
                                Boolean hot) {
}
