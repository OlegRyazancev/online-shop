package com.ryazancev.customer.util.processor;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.common.dto.user.UserUpdateRequest;
import com.ryazancev.customer.kafka.CustomerProducerService;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.util.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class KafkaMessageProcessor {

    private final CustomerProducerService customerProducerService;
    private final NotificationProcessor notificationProcessor;

    @Async("asyncTaskExecutor")
    public void sendUpdateUserRequestToAuthUpdateTopic(
            final Customer customer) {

        UserUpdateRequest request = UserUpdateRequest.builder()
                .customerId(customer.getId())
                .email(customer.getEmail())
                .name(customer.getUsername())
                .build();

        customerProducerService.sendMessageToAuthUpdateTopic(request);
    }

    @Async("asyncTaskExecutor")
    public void sendCustomerIdToAuthDeleteTopic(final Long id) {

        customerProducerService.sendMessageToAuthDeleteTopic(id);
    }

    @Async("asyncTaskExecutor")
    public void sendPurchaseProcessedNotification(
            final PurchaseEditDto purchaseEditDto,
            final RequestHeader requestHeader) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                purchaseEditDto,
                                requestHeader
                        );

        customerProducerService.sendNotification(privateNotificationRequest);
    }
}
