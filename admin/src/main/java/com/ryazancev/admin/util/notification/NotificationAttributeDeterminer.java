package com.ryazancev.admin.util.notification;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.ClientsService;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
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
public class NotificationAttributeDeterminer {

    private final ClientsService clientsService;
    private final HttpServletRequest servletRequest;

    public Long determineSenderId() {

        return Long.valueOf(servletRequest.getHeader("userId"));
    }

    public Long determineSenderId(NotificationScope scope,

                                  Long recipientId) {
        return scope == NotificationScope.ADMIN ?
                recipientId : determineSenderId();
    }

    public Long determineRecipientId(ObjectType objectType,
                                     Long objectId,
                                     NotificationScope notificationScope) {

        if (notificationScope == NotificationScope.PRIVATE) {
            switch (objectType) {
                case PRODUCT -> {

                    return clientsService.getOwnerIdByProductId(objectId);
                }
                case ORGANIZATION -> {

                    return clientsService.getOwnerIdByOrganizationId(objectId);
                }
            }
        }
        return null;
    }

    public Properties determineProperties(ObjectType objectType,
                                          Long objectId,
                                          NotificationType notificationType) {

        Properties properties = new Properties();

        if (notificationType ==
                NotificationType.ADMIN_NEW_REGISTRATION_REQUEST_RECEIVED) {

            properties.setProperty(
                    "object_type",
                    objectType.toString()
            );
            return properties;
        }

        switch (objectType) {

            case PRODUCT -> {

                setProductProperties(
                        objectType,
                        objectId,
                        notificationType,
                        properties);
            }
            case ORGANIZATION -> {

                setOrganizationProperties(
                        objectType,
                        objectId,
                        notificationType,
                        properties);
            }
        }
        return properties;
    }

    private void setOrganizationProperties(ObjectType objectType,
                                           Long objectId,
                                           NotificationType notificationType,
                                           Properties properties) {

        OrganizationDto organizationDto = clientsService
                .getSimpleOrganizationById(objectId)
                .safelyCast(OrganizationDto.class, false);

        properties.setProperty(
                "organization_name", organizationDto.getName());

        if (notificationType ==
                NotificationType.PRIVATE_ACTIVATE_OBJECT
                || notificationType ==
                NotificationType.PRIVATE_FREEZE_OBJECT) {

            properties.setProperty(
                    "object_type", objectType.toString());
            properties.setProperty(
                    "object_name", organizationDto.getName());
        }
    }

    private void setProductProperties(ObjectType objectType,
                                      Long objectId,
                                      NotificationType notificationType,
                                      Properties properties) {

        ProductDto productDto = clientsService
                .getSimpleProductById(objectId)
                .safelyCast(ProductDto.class, false);

        properties.setProperty(
                "product_name", productDto.getProductName()
        );

        if (notificationType ==
                NotificationType.PUBLIC_NEW_PRODUCT_CREATED) {

            properties.setProperty(
                    "product_price", productDto.getPrice().toString()
            );
        }
        if (notificationType ==
                NotificationType.PRIVATE_ACTIVATE_OBJECT
                || notificationType ==
                NotificationType.PRIVATE_FREEZE_OBJECT) {

            properties.setProperty(
                    "object_type", objectType.toString()
            );
            properties.setProperty(
                    "object_name", productDto.getProductName()
            );
        }
    }

    public NotificationType determineNotificationType(
            RegistrationRequest request, NotificationScope scope) {

        if (scope == NotificationScope.ADMIN) {
            return NotificationType.ADMIN_NEW_REGISTRATION_REQUEST_RECEIVED;
        } else if (scope == NotificationScope.PUBLIC
                && request.getStatus() == RequestStatus.ACCEPTED
                && request.getObjectType() == ObjectType.PRODUCT) {
            return NotificationType.PUBLIC_NEW_PRODUCT_CREATED;
        } else {
            switch (request.getObjectType()) {
                case PRODUCT -> {

                    return request.getStatus() == RequestStatus.ACCEPTED ?
                            NotificationType
                                    .PRIVATE_PRODUCT_REGISTRATION_ACCEPTED
                            :
                            NotificationType
                                    .PRIVATE_PRODUCT_REGISTRATION_REJECTED;
                }
                case ORGANIZATION -> {

                    return request.getStatus() == RequestStatus.ACCEPTED ?
                            NotificationType
                                    .PRIVATE_ORGANIZATION_REGISTRATION_ACCEPTED
                            :
                            NotificationType
                                    .PRIVATE_ORGANIZATION_REGISTRATION_REJECTED;
                }
                default -> {

                    return null;
                }
            }
        }
    }

    public NotificationType determineNotificationType(ObjectRequest request) {

        return switch (request.getObjectStatus()) {
            case ACTIVATE -> NotificationType.PRIVATE_ACTIVATE_OBJECT;
            case FREEZE -> NotificationType.PRIVATE_FREEZE_OBJECT;
        };
    }

    public NotificationType determineNotificationType(UserLockRequest request) {

        return request.isLock() ?
                NotificationType.PRIVATE_ACCOUNT_LOCKED
                : NotificationType.PRIVATE_ACCOUNT_UNLOCKED;
    }
}
