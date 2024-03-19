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

    public Long determineSenderId(final NotificationScope scope,
                                  final Long recipientId) {

        return scope == NotificationScope.ADMIN
                ? recipientId
                : determineSenderId();
    }

    public Long determineRecipientId(final ObjectType objectType,
                                     final Long objectId,
                                     final NotificationScope scope) {

        if (scope == NotificationScope.PRIVATE) {
            switch (objectType) {
                case PRODUCT -> {

                    return clientsService.getOwnerIdByProductId(objectId);
                }
                case ORGANIZATION -> {

                    return clientsService.getOwnerIdByOrganizationId(objectId);
                }
                default -> {
                }
            }
        }
        return null;
    }

    public Properties determineProperties(final ObjectType objectType,
                                          final Long objectId,
                                          final NotificationType type) {

        Properties properties = new Properties();

        switch (objectType) {

            case PRODUCT -> {

                setProductProperties(
                        objectType,
                        objectId,
                        type,
                        properties);
            }
            case ORGANIZATION -> {

                setOrganizationProperties(
                        objectType,
                        objectId,
                        type,
                        properties);
            }
            default -> {
            }
        }
        return properties;
    }

    private void setOrganizationProperties(final ObjectType objectType,
                                           final Long objectId,
                                           final NotificationType type,
                                           final Properties properties) {

        OrganizationDto organizationDto = clientsService
                .getSimpleOrganizationById(objectId)
                .safelyCast(OrganizationDto.class, false);

        properties.setProperty(
                "organization_name", organizationDto.getName());

        if (type == NotificationType.PRIVATE_ACTIVATE_OBJECT
                || type == NotificationType.PRIVATE_FREEZE_OBJECT) {

            properties.setProperty(
                    "object_type", objectType.name());
            properties.setProperty(
                    "object_name", organizationDto.getName());
        }
    }

    private void setProductProperties(final ObjectType objectType,
                                      final Long objectId,
                                      final NotificationType type,
                                      final Properties properties) {

        ProductDto productDto = clientsService
                .getSimpleProductById(objectId)
                .safelyCast(ProductDto.class, false);

        properties.setProperty(
                "product_name", productDto.getProductName()
        );

        if (type == NotificationType.PUBLIC_NEW_PRODUCT_CREATED) {

            properties.setProperty(
                    "product_price", productDto.getPrice().toString()
            );
        }
        if (type == NotificationType.PRIVATE_ACTIVATE_OBJECT
                || type == NotificationType.PRIVATE_FREEZE_OBJECT) {

            properties.setProperty(
                    "object_type", objectType.name()
            );
            properties.setProperty(
                    "object_name", productDto.getProductName()
            );
        }
    }

    public NotificationType determineNotificationType(
            final RegistrationRequest request,
            final NotificationScope scope) {

        if (scope == NotificationScope.PUBLIC
                && request.getStatus() == RequestStatus.ACCEPTED
                && request.getObjectType() == ObjectType.PRODUCT) {
            return NotificationType.PUBLIC_NEW_PRODUCT_CREATED;
        } else {
            switch (request.getObjectType()) {
                case PRODUCT -> {

                    return request.getStatus() == RequestStatus.ACCEPTED
                            ?
                            NotificationType
                                    .PRIVATE_PRODUCT_REGISTRATION_ACCEPTED
                            :
                            NotificationType
                                    .PRIVATE_PRODUCT_REGISTRATION_REJECTED;
                }
                case ORGANIZATION -> {

                    return request.getStatus() == RequestStatus.ACCEPTED
                            ?
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

    public NotificationType determineNotificationType(
            final ObjectRequest request) {

        return switch (request.getObjectStatus()) {
            case ACTIVATE -> NotificationType.PRIVATE_ACTIVATE_OBJECT;
            case FREEZE -> NotificationType.PRIVATE_FREEZE_OBJECT;
        };
    }

    public NotificationType determineNotificationType(
            final UserLockRequest request) {

        return request.isLock()
                ? NotificationType.PRIVATE_ACCOUNT_LOCKED
                : NotificationType.PRIVATE_ACCOUNT_UNLOCKED;
    }
}
