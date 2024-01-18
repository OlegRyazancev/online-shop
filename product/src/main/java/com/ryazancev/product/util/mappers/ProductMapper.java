package com.ryazancev.product.util.mappers;

import com.ryazancev.clients.product.ProductDTO;
import com.ryazancev.clients.product.ProductInfoDTO;
import com.ryazancev.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    List<ProductDTO> toDTO(List<Product> products);

    ProductInfoDTO toDTO(Product product);
}
