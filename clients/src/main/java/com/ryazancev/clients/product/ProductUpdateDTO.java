package com.ryazancev.clients.product;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateDTO {


    private Long id;

    private String productName;

    private String description;

    private Double price;

    private Integer quantityInStock;

    private List<String> keywords;
}
