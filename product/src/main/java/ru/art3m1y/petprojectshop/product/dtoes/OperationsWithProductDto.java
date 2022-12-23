package ru.art3m1y.petprojectshop.product.dtoes;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class OperationsWithProductDto {
    @NotEmpty(message = "Название товара не может быть пустым")
    private String name;
    @Min(value = 0, message = "Цена должна быть больше 0 рублей")
    @Max(value = 10000000, message = "Цена не должна превышать 10000000 рублей")
    private int cost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
