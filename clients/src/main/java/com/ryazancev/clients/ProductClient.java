package com.ryazancev.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product")
public interface ProductClient {
}
