package com.ryazancev.product.util.mapper;

import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.product.ProductEditDto;
import com.ryazancev.product.model.Product;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    @Named("toSimpleDto")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "quantityInStock", ignore = true)
    @Mapping(target = "keywords", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    ProductDto toSimpleDto(Product existing);

    @IterableMapping(qualifiedByName = "toSimpleDto")
    List<ProductDto> toSimpleListDto(List<Product> products);

    @Mappings({
            @Mapping(
                    target = "keywords",
                    source = "keywords",
                    qualifiedByName = "keywordsToString"
            )
    })
    Product toEntity(ProductEditDto productEditDto);

    @Mappings({
            @Mapping(
                    target = "keywords",
                    source = "keywords",
                    qualifiedByName = "stringToKeywords"
            ),
    })
    ProductDto toDetailedDto(Product product);

    @Named("stringToKeywords")
    default List<String> stringToKeywords(String string) {
        return Arrays.asList(string.split(", "));
    }

    @Named("keywordsToString")
    default String keywordsToString(List<String> stringList) {
        return String.join(", ", stringList);
    }
}
