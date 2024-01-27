package com.ryazancev.clients.product;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSimpleDTO {

    private Long id;
    private String productName;
}
