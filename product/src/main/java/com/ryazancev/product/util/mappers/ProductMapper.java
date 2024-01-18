package com.ryazancev.product.util.mappers;

import com.ryazancev.clients.ProductDTO;
import com.ryazancev.clients.ProductInfoDTO;
import com.ryazancev.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    List<ProductDTO> toDTO(List<Product> products);

    ProductInfoDTO toDTO(Product product);
}
