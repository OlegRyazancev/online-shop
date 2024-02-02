package com.ryazancev.dto.product;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateQuantityRequest {

    private Long productId;
    private Integer quantityInStock;
}
