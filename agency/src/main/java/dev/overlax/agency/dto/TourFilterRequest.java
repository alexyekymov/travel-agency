package dev.overlax.agency.dto;

import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;

import java.math.BigDecimal;

public record TourFilterRequest(TourType tourType,
                                TransferType transferType,
                                HotelType hotelType,
                                BigDecimal minPrice,
                                BigDecimal maxPrice,
                                Boolean hot) {
}
