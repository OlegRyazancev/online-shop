package com.ryazancev.product.util.mapper;

import com.ryazancev.dto.product.ProductDTO;
import com.ryazancev.dto.product.ProductEditDTO;
import com.ryazancev.product.model.Product;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    @Named("toSimpleDTO")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "quantityInStock", ignore = true)
    @Mapping(target = "keywords", ignore = true)
    ProductDTO toSimpleDTO(Product existing);

    @IterableMapping(qualifiedByName = "toSimpleDTO")
    List<ProductDTO> toSimpleListDTO(List<Product> products);

    @Mappings({
            @Mapping(
                    target = "keywords",
                    source = "keywords",
                    qualifiedByName = "keywordsToString"
            )
    })
    Product toEntity(ProductEditDTO productEditDTO);

    @Mappings({
            @Mapping(
                    target = "keywords",
                    source = "keywords",
                    qualifiedByName = "stringToKeywords"
            ),
    })
    ProductDTO toDetailedDTO(Product product);

    @Named("stringToKeywords")
    default List<String> stringToKeywords(String string) {
        return Arrays.asList(string.split(", "));
    }

    @Named("keywordsToString")
    default String keywordsToString(List<String> stringList) {
        return String.join(", ", stringList);
    }

}
