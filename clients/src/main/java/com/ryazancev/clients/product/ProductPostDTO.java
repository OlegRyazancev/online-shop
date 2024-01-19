package com.ryazancev.clients.product;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPostDTO {

    private Long id;

    private String name;

    private String description;

    private Long organizationId;

    private Double price;

    private Integer quantityInStock;

    private List<String> keywords;

    //todo: discount field

}
