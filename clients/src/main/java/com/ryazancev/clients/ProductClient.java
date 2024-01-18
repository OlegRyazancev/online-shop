package com.ryazancev.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("product")
public interface ProductClient {
}
