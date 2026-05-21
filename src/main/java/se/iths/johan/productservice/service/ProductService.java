package se.iths.johan.productservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.iths.johan.productservice.dto.ProductOrderRequestDto;
import se.iths.johan.productservice.dto.ProductRequestDto;
import se.iths.johan.productservice.dto.ProductResponseDto;
import se.iths.johan.productservice.mapper.ProductMapper;
import se.iths.johan.productservice.model.Product;
import se.iths.johan.productservice.repository.ProductRepository;

import java.util.ArrayList;
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
                .map(product ->  productMapper.toDto(product));
    }

    public ProductResponseDto create(ProductRequestDto requestDto) {
        Product product = productMapper.toEntity(requestDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);

    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }


    // Använder @Transactional så att den gör allt eller inget
    // dena metod skicka tillbaka en lista med ProductResponseDto.
    // Den tar emot en dto lista från order service "ProductOrderRequestDto" som vi kallar "requestStock"
    @Transactional
    public List<ProductResponseDto> decreaseStock(List<ProductOrderRequestDto> requestStock) {

        // Skapar en lista som vi sedan kan skicka tillbaka till order-service
        List<ProductResponseDto> responceList = new ArrayList<>();

        // skapar en loop
        for (ProductOrderRequestDto request : requestStock){

            // tar ut en ett object/product ur requestStock listan
            Product product = productRepository.findById(request.id())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            // om det inte finns tillräkligt i lagger så kastar vi Exception
            if (product.getStock() < request.quantity()){
                throw new IllegalArgumentException("Product quantity exceeded");
            }

            // Om allt innan gick bra så minskar vi stock med quantity från order-service och sparar till product repository
            product.setStock(product.getStock() - request.quantity());
            productRepository.save(product);

            // omvandlar product till ProductResponseDto som vi sparar till responceList som vi ska skicka tillbaka till order-service
            ProductResponseDto dto = productMapper.toDto(product);
            responceList.add(dto);


        }
        //Skickar listan med ProductResponseDto's
        return responceList;


    }

}
