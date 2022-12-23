package ru.art3m1y.petprojectshop.product.utils.exceptions;

public class ProductNoFoundException extends RuntimeException {

    public ProductNoFoundException() {
        super("Продукт с таким идентификатором не найден");
    }
}
