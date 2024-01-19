package com.ryazancev.product.controller;

import com.ryazancev.clients.product.ProductInfoDTO;
import com.ryazancev.clients.product.ProductPostDTO;
import com.ryazancev.clients.product.ProductsGetResponse;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductsGetResponse> getAvailableProducts() {
        ProductsGetResponse products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductInfoDTO> getProductInfo(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }

    @PostMapping
    public ResponseEntity<ProductInfoDTO> save(@RequestBody ProductPostDTO productPostDTO) {
        productService.save(productPostDTO);
        return ResponseEntity.ok(productService.save(productPostDTO));
        //todo:refactor
    }


    //todo: get Reviews and rating of a product (communicate to review service)
    //todo: set Review and rating of a product (communicate to review service)

}
