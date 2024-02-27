package com.ryazancev.common.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.common.dto.Element;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Product DTO. Used in GET methods")
public class ProductDto implements Element {

    @Schema(
            description = "Product ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the product",
            example = "Example name"
    )
    private String productName;

    @Schema(
            description = "Description of the product",
            example = "Example description of good product"
    )
    private String description;

    @Schema(description = "Details of the issuer of product")
    private Element organization;

    @Schema(
            description = "Price of the product",
            example = "123.2"
    )
    private Double price;

    @Schema(
            description = "Quantity in stock of the product",
            example = "123"
    )
    private Integer quantityInStock;

    @Schema(
            description = "Keywords associated with the product",
            example = "[\"electronics\", \"gadget\", \"smartphone\"]"
    )
    private List<String> keywords;

    @Schema(
            description = "Average rating of the product. 1-5",
            example = "2.3"
    )
    private Double averageRating;

    @Schema(
            description = "Status of the product (e.g., ACTIVE, INACTIVE)",
            example = "ACTIVE"
    )
    private String status;

    @Schema(
            description = "Date and time when the product was registered",
            example = "2024-05-22T22:15:21"
    )
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.TIME
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registeredAt;

}
