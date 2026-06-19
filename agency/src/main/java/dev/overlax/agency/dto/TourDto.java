package dev.overlax.agency.dto;

import java.math.BigDecimal;

public record TourDto(String title, String description, BigDecimal price, String transferType) {
}
