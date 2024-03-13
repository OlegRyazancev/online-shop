package com.ryazancev.admin.util;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.ClientsService;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.common.dto.organization.OrganizationDto;
import com.ryazancev.common.dto.product.ProductDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationProcessor {

    private final HttpServletRequest httpServletRequest;
    private final ClientsService clientsService;


    public NotificationRequest createNotification(RegistrationRequest request,
                                                  NotificationScope scope) {

        NotificationType type = determineNotificationType(request, scope);
        Long recipientId = determineRecipientId(request, scope);
        Long senderId = Long.valueOf(httpServletRequest.getHeader("userId"));
        Properties properties = determineProperties(request, type);

        return NotificationRequest.builder()
                .scope(scope)
                .type(type)
                .properties(properties)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
    }

    private Long determineRecipientId(RegistrationRequest request,
                                      NotificationScope scope) {

        if (scope.equals(NotificationScope.PRIVATE)) {
            if (request.getObjectType().equals(ObjectType.PRODUCT)) {

                return clientsService.getOwnerIdByProductId(
                        request.getObjectToRegisterId());

            } else if (request.getObjectType().equals(ObjectType.ORGANIZATION)) {

                return clientsService.getOwnerIdByOrganizationId(
                        request.getObjectToRegisterId());
            }

        }

        return null;
    }

    private Properties determineProperties(RegistrationRequest request,
                                           NotificationType type) {

        Properties properties = new Properties();

        if (request.getObjectType().equals(ObjectType.PRODUCT)) {

            ProductDto productDto = clientsService
                    .getSimpleProductById(request.getObjectToRegisterId())
                    .safelyCast(ProductDto.class, false);

            properties.setProperty(
                    "product_name",
                    productDto.getProductName()
            );

            if (type.equals(NotificationType.PUBLIC_NEW_PRODUCT_CREATED)) {

                properties.setProperty(
                        "product_price",
                        productDto.getPrice().toString()
                );
            }
        } else if (request.getObjectType().equals(ObjectType.ORGANIZATION)) {

            OrganizationDto organizationDto = clientsService
                    .getSimpleOrganizationById(request.getObjectToRegisterId())
                    .safelyCast(OrganizationDto.class, false);

            properties.setProperty(
                    "organization_name",
                    organizationDto.getName()
            );
        }
        return properties;

    }

    private NotificationType determineNotificationType(
            RegistrationRequest request, NotificationScope scope) {

        if (scope.equals(NotificationScope.PUBLIC)
                && request.getStatus().equals(RequestStatus.ACCEPTED)) {

            return NotificationType.PUBLIC_NEW_PRODUCT_CREATED;
        } else if (request.getObjectType().equals(ObjectType.PRODUCT)) {

            return request.getStatus().equals(RequestStatus.ACCEPTED)
                    ?
                    NotificationType.PRIVATE_PRODUCT_REGISTRATION_ACCEPTED
                    :
                    NotificationType.PRIVATE_PRODUCT_REGISTRATION_REJECTED;

        } else if (request.getObjectType().equals(ObjectType.ORGANIZATION)) {

            return request.getStatus().equals(RequestStatus.ACCEPTED)
                    ?
                    NotificationType.PRIVATE_ORGANIZATION_REGISTRATION_ACCEPTED
                    :
                    NotificationType.PRIVATE_ORGANIZATION_REGISTRATION_REJECTED;
        }

        return null;
    }

}
