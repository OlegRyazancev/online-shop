package com.ryazancev.customer.util.processor;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.common.dto.user.UserUpdateRequest;
import com.ryazancev.customer.kafka.CustomerProducerService;
import com.ryazancev.customer.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class KafkaMessageProcessor {

    private final CustomerProducerService customerProducerService;
    private final NotificationProcessor notificationProcessor;

    public void sendUpdateUserRequestToAuthUpdateTopic(Customer customer) {

        UserUpdateRequest request = UserUpdateRequest.builder()
                .customerId(customer.getId())
                .email(customer.getEmail())
                .name(customer.getUsername())
                .build();

        customerProducerService.sendMessageToAuthUpdateTopic(request);
    }

    public void sendCustomerIdToAuthDeleteTopic(Long id) {

        customerProducerService.sendMessageToAuthDeleteTopic(id);
    }

    public void sendPurchaseProcessedNotification(
            PurchaseEditDto purchaseEditDto) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor.createNotification(purchaseEditDto);

        customerProducerService.sendNotification(privateNotificationRequest);
    }
}
