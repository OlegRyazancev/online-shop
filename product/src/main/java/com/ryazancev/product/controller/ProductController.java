package com.ryazancev.product.controller;

import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    //todo: getAvailableProducts (id, name;)
    //todo: get detailed info about product: (other fields)


    //todo: get Reviews and rating of a product (communicate to review service)
    //todo: leave Reviews and rating of a product (communicate to review service)

}
