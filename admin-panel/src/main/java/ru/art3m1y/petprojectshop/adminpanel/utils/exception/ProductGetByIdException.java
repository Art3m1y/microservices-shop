package ru.art3m1y.petprojectshop.adminpanel.utils.exception;

public class ProductGetByIdException extends RuntimeException{
    public ProductGetByIdException() {
        super("Не удалось получить продукт по его идентификатору");
    }
}
