package se.iths.johan.productservice.dto;

public record ProductOrderRequestDto(
        Long id,
        int quantity
) {
}
