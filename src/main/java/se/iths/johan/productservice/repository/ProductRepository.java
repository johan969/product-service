package se.iths.johan.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.johan.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
