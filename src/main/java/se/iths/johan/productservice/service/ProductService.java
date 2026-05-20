package se.iths.johan.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.iths.johan.productservice.dto.ProductRequestDto;
import se.iths.johan.productservice.dto.ProductResponseDto;
import se.iths.johan.productservice.mapper.ProductMapper;
import se.iths.johan.productservice.model.Product;
import se.iths.johan.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public List<ProductResponseDto> findAll() {
        List <Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toDto).toList();
    }

    public Optional<ProductResponseDto> findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto);
    }

    public ProductResponseDto create(ProductRequestDto requestDto) {
        Product product = productMapper.toEntity(requestDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);

    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
