package se.iths.johan.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.iths.johan.productservice.dto.ProductRequestDto;
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

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product create(ProductRequestDto requestDto) {
        Product product = productMapper.toEntity(requestDto);
       return productRepository.save(product);

    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
