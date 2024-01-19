package com.ryazancev.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description", unique = true)
    private String description;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;

    @Column(name = "keywords")
    private String keywords;

    //todo: discount field
}

