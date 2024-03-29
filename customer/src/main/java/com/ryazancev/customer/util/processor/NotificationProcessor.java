package com.ryazancev.customer.util.processor;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.customer.service.ClientsService;
import com.ryazancev.customer.util.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationProcessor {

    private final ClientsService clientsService;

    public NotificationRequest createNotification(
            final PurchaseEditDto purchaseEditDto,
            final RequestHeader requestHeader) {

        Long recipientId = (Long) clientsService
                .getProductOwnerId(purchaseEditDto.getProductId());

        Properties properties = new Properties();

        String productName = clientsService
                .getSimpleProductById(purchaseEditDto.getProductId())
                .safelyCast(ProductDto.class, false)
                .getProductName();

        properties.setProperty("product_name", productName);

        return NotificationRequest.builder()
                .scope(NotificationScope.PRIVATE)
                .type(NotificationType.PRIVATE_PURCHASE_PROCESSED)
                .recipientId(recipientId)
                .senderId(requestHeader.getUserId())
                .properties(properties)
                .build();

    }
}
