package se.iths.johan.productservice.dto;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        double price,
        int stock
) {
}
