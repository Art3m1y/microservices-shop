package ru.art3m1y.petprojectshop.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.art3m1y.petprojectshop.product.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
