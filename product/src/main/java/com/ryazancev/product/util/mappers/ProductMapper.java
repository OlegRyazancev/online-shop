package com.ryazancev.product.util.mappers;

import com.ryazancev.clients.product.ProductDTO;
import com.ryazancev.clients.product.ProductInfoDTO;
import com.ryazancev.clients.product.ProductPostDTO;
import com.ryazancev.product.model.Product;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    List<ProductDTO> toDTO(List<Product> products);

    @Mappings({
            @Mapping(target = "keywords", source = "keywords", qualifiedByName = "stringListToString"),
    })
    Product toEntity(ProductPostDTO productPostDTO);

    @Mappings({
            @Mapping(target = "keywords", source = "keywords", qualifiedByName = "keywordsToString"),
    })
    ProductInfoDTO toDTO(Product product);

    @Named("keywordsToString")
    default List<String> keywordsToString(String string) {
        return Arrays.asList(string.split(", "));
    }

    @Named("stringListToString")
    default String stringListToString(List<String> stringList) {
        return String.join(", ", stringList);
    }
}
