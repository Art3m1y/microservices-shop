package ru.art3m1y.petprojectshop.product.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.art3m1y.petprojectshop.product.modelmappers.ProductModelMapper;
import ru.art3m1y.petprojectshop.product.models.Product;
import ru.art3m1y.petprojectshop.product.services.ProductService;
import ru.art3m1y.petprojectshop.product.utils.exceptions.ErrorResponse;
import ru.art3m1y.petprojectshop.product.utils.exceptions.ProductNoFoundException;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductModelMapper productModelMapper;

    public ProductController(ProductService productService, ProductModelMapper productModelMapper) {
        this.productService = productService;
        this.productModelMapper = productModelMapper;
    }

    @GetMapping("/products")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Product> getProducts() {
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.findById(Long.parseLong(id));
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ErrorResponse handlerException(NumberFormatException e) {
        return new ErrorResponse("Ошибка преобразования в число", System.currentTimeMillis());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    private ErrorResponse handlerException(ProductNoFoundException e) {
        return new ErrorResponse(e.getMessage(), System.currentTimeMillis());
    }
}
