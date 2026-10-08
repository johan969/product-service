package se.iths.johan.productservice.dto;

import se.iths.johan.productservice.model.Category;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String imageUrl,
        Category category
) {
}
