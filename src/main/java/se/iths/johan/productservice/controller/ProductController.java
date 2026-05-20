package se.iths.johan.productservice.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.iths.johan.productservice.dto.ProductOrderRequestDto;
import se.iths.johan.productservice.dto.ProductRequestDto;
import se.iths.johan.productservice.dto.ProductResponseDto;
import se.iths.johan.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity <List<ProductResponseDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) {
        return productService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto dto ) {
        ProductResponseDto responseDto = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();

    }


    // Skickar tillbaka en lista med ProductResponseDto, tar emot en lista med ProductOrderRequestDto från order-service
    @PostMapping("/stock")
    public ResponseEntity<List<ProductResponseDto>> decreaseStock(@RequestBody List<ProductOrderRequestDto> requestStock) {

        // Skickar vår ProductResponseDto lista till productService som ger oss en updaterad ProductResponseDto lista
        List<ProductResponseDto> responseDto = productService.decreaseStock(requestStock);
        return ResponseEntity.ok(responseDto);

    }

}
