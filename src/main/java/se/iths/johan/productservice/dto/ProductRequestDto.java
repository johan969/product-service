package se.iths.johan.productservice.dto;

public record ProductRequestDto(
        String name,
        String description,
        double price,
        int stock
) {


}
