package com.ryazancev.product.service;

import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.product.UpdateQuantityRequest;


public interface KafkaListeners {

    void updateQuantity(UpdateQuantityRequest request);

    void changeStatusAndRegister(RegistrationRequestDTO requestDTO);
}
