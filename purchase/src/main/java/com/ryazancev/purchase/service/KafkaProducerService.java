package com.ryazancev.purchase.service;

import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.product.UpdateQuantityRequest;

public interface KafkaProducerService {

    void sendMessageToProductTopic(UpdateQuantityRequest message);

    void sendMessageToCustomerTopic(UpdateBalanceRequest message);
}
