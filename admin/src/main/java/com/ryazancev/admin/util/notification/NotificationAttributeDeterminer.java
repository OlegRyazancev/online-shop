package com.ryazancev.admin.util.notification;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.ClientsService;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.admin.enums.ObjectStatus;
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

    public Long determineRecipientId(ObjectType objectType,
                                     Long objectId,
                                     NotificationScope snotificationScope) {

        if (snotificationScope == NotificationScope.PRIVATE) {
            if (objectType == ObjectType.PRODUCT) {

                return clientsService.getOwnerIdByProductId(objectId);

            } else if (objectType == ObjectType.ORGANIZATION) {

                return clientsService.getOwnerIdByOrganizationId(objectId);
            }
        }
        return null;
    }

    public Properties determineProperties(ObjectType objectType,
                                          Long objectId,
                                          NotificationType notificationType) {

        Properties properties = new Properties();

        if (objectType == ObjectType.PRODUCT) {

            ProductDto productDto = clientsService
                    .getSimpleProductById(objectId)
                    .safelyCast(ProductDto.class, false);

            properties.setProperty(
                    "product_name", productDto.getProductName()
            );

            if (notificationType == NotificationType.PUBLIC_NEW_PRODUCT_CREATED) {

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
        } else if (objectType == ObjectType.ORGANIZATION) {

            OrganizationDto organizationDto = clientsService
                    .getSimpleOrganizationById(objectId)
                    .safelyCast(OrganizationDto.class, false);

            properties.setProperty(
                    "organization_name", organizationDto.getName()
            );

            if (notificationType ==
                    NotificationType.PRIVATE_ACTIVATE_OBJECT
                    || notificationType ==
                    NotificationType.PRIVATE_FREEZE_OBJECT) {

                properties.setProperty(
                        "object_type", objectType.toString()
                );
                properties.setProperty(
                        "object_name", organizationDto.getName()
                );
            }
        }
        return properties;

    }

    public NotificationType determineNotificationType(
            RegistrationRequest request, NotificationScope scope) {

        if (scope == NotificationScope.PUBLIC
                && request.getStatus() == RequestStatus.ACCEPTED) {

            return NotificationType.PUBLIC_NEW_PRODUCT_CREATED;
        } else if (request.getObjectType() == ObjectType.PRODUCT) {

            return request.getStatus() == RequestStatus.ACCEPTED
                    ?
                    NotificationType.PRIVATE_PRODUCT_REGISTRATION_ACCEPTED
                    :
                    NotificationType.PRIVATE_PRODUCT_REGISTRATION_REJECTED;

        } else if (request.getObjectType() == ObjectType.ORGANIZATION) {

            return request.getStatus() == RequestStatus.ACCEPTED
                    ?
                    NotificationType.PRIVATE_ORGANIZATION_REGISTRATION_ACCEPTED
                    :
                    NotificationType.PRIVATE_ORGANIZATION_REGISTRATION_REJECTED;
        }

        return null;
    }

    public NotificationType determineNotificationType(
            ObjectRequest request) {

        if (request.getObjectStatus() == ObjectStatus.ACTIVATE) {

            return NotificationType.PRIVATE_ACTIVATE_OBJECT;
        } else if (request.getObjectStatus() == ObjectStatus.FREEZE) {

            return NotificationType.PRIVATE_FREEZE_OBJECT;
        }
        return null;
    }

    public NotificationType determineNotificationType(
            UserLockRequest request) {

        if (request.isLock()) {

            return NotificationType.PRIVATE_ACCOUNT_LOCKED;
        } else {

            return NotificationType.PRIVATE_ACCOUNT_UNLOCKED;
        }
    }
}
