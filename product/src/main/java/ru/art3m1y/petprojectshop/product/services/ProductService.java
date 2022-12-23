package ru.art3m1y.petprojectshop.product.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.art3m1y.petprojectshop.product.models.Product;
import ru.art3m1y.petprojectshop.product.repositories.ProductRepository;
import ru.art3m1y.petprojectshop.product.utils.exceptions.ProductNoFoundException;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow(ProductNoFoundException::new);
    }
}
