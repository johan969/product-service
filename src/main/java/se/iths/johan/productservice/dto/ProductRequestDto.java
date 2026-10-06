package se.iths.johan.productservice.dto;

import java.math.BigDecimal;

public record ProductRequestDto(
        String name,
        String description,
        BigDecimal price,
        int stock,
        String category,
        String imageUrl
) {


}
