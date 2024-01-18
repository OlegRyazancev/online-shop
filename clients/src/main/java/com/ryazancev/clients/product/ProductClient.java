package com.ryazancev.clients.product;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product")
public interface ProductClient {
}
