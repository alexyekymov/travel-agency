package dev.overlax.agency.dto;

import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TourRequest(String title, String description, BigDecimal price,
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate,
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate evictionDate,
                          TourType tourType, HotelType hotelType, TransferType transferType,
                          Integer availableSeats, MultipartFile image) {
}
