package com.ryazancev.clients.purchase;

import com.ryazancev.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "purchase",
        configuration = FeignClientsConfiguration.class
)
public interface PurchaseClient {
}
