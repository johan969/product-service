package se.iths.johan.productservice.dto;

import se.iths.johan.productservice.model.Category;

import java.math.BigDecimal;

public record ProductRequestDto(
        String name,
        String description,
        BigDecimal price,
        int stock,
        String imageUrl,
        Category category

) {


}
