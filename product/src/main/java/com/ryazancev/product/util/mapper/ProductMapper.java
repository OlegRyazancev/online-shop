package com.ryazancev.product.util.mapper;

import com.ryazancev.clients.product.ProductCreateDTO;
import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductSimpleDTO;
import com.ryazancev.product.model.Product;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    ProductSimpleDTO toSimpleDTO(Product existing);

    List<ProductSimpleDTO> toListDTO(List<Product> products);

    @Mappings({
            @Mapping(
                    target = "keywords",
                    source = "keywords",
                    qualifiedByName = "keywordsToString"
            )
    })
    Product toEntity(ProductCreateDTO productCreateDTO);

    @Mappings({
            @Mapping(
                    target = "keywords",
                    source = "keywords",
                    qualifiedByName = "stringToKeywords"
            ),
    })
    ProductDetailedDTO toDetailedDTO(Product product);

    @Named("stringToKeywords")
    default List<String> stringToKeywords(String string) {
        return Arrays.asList(string.split(", "));
    }

    @Named("keywordsToString")
    default String keywordsToString(List<String> stringList) {
        return String.join(", ", stringList);
    }

}
