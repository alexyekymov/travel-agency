package dev.overlax.agency.dto;

import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;

import java.math.BigDecimal;
import java.util.UUID;

public record TourResponse(UUID id, String title, String description, BigDecimal price, TourType tourType,
                           HotelType hotelType, TransferType transferType, Boolean hot, String imageName) {
}
