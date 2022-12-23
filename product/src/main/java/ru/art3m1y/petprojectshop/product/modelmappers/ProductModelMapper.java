package ru.art3m1y.petprojectshop.product.modelmappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.art3m1y.petprojectshop.product.dtoes.OperationsWithProductDto;
import ru.art3m1y.petprojectshop.product.models.Product;

@Component
public class ProductModelMapper {
    private final ModelMapper modelMapper;

    public ProductModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product convertToProduct(OperationsWithProductDto operationsWithProductDto) {
        return modelMapper.map(operationsWithProductDto, Product.class);
    }

    public OperationsWithProductDto convertToOperationWithProductDto(Product product) {
        return modelMapper.map(product, OperationsWithProductDto.class);
    }
}
