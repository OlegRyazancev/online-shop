package com.ryazancev.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Purchase implements Serializable {

    @Id
    private String id;
    private Long customerId;
    private Long productId;
    private Double amount;
    private LocalDateTime purchaseDate;

}
