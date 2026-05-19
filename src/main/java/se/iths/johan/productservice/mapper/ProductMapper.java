package se.iths.johan.productservice.mapper;

import org.mapstruct.Mapper;
import se.iths.johan.productservice.dto.ProductRequestDto;
import se.iths.johan.productservice.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDto productRequestDto);
}
