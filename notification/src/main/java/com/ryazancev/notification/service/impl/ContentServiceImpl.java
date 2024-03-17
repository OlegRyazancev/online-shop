package com.ryazancev.notification.service.impl;

import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.notification.model.Content;
import com.ryazancev.notification.service.ContentService;
import com.ryazancev.notification.util.processor.ContentProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentProcessor contentProcessor;

    @Override
    public Content generateContent(NotificationType type,
                                   Properties properties) {
        Content content;

        switch (type) {
            case PRIVATE_PRODUCT_REGISTRATION_ACCEPTED -> {
                content = contentProcessor
                        .createProductRegistrationAcceptedContent(
                                properties);
            }
            case PRIVATE_PRODUCT_REGISTRATION_REJECTED -> {
                content = contentProcessor
                        .createProductRegistrationRejectedContent(
                                properties);
            }
            case PRIVATE_ORGANIZATION_REGISTRATION_ACCEPTED -> {
                content = contentProcessor
                        .createOrganizationRegistrationAcceptedContent(
                                properties);
            }
            case PRIVATE_ORGANIZATION_REGISTRATION_REJECTED -> {
                content = contentProcessor
                        .createOrganizationRegistrationRejectedContent(
                                properties);
            }
            case PRIVATE_ACCOUNT_LOCKED -> {
                content = contentProcessor
                        .createAccountLockedContent();
            }
            case PRIVATE_ACCOUNT_UNLOCKED -> {
                content = contentProcessor
                        .createAccountUnlockedContent();
            }
            case PRIVATE_REVIEW_CREATED -> {
                content = contentProcessor
                        .createReviewCreatedContent(
                                properties);
            }
            case PRIVATE_PURCHASE_PROCESSED -> {
                content = contentProcessor
                        .createPurchaseProcessedContent(
                                properties);
            }
            case PRIVATE_FREEZE_OBJECT -> {
                content = contentProcessor
                        .createFreezeObjectContent(
                                properties);
            }
            case PRIVATE_ACTIVATE_OBJECT -> {
                content = contentProcessor
                        .createActivateObjectContent(
                                properties);
            }
            case PUBLIC_NEW_PRODUCT_CREATED -> {
                content = contentProcessor
                        .createNewProductAvailableContent(
                                properties);
            }
            case ADMIN_NEW_REGISTRATION_REQUEST_RECEIVED -> {
                content = contentProcessor
                        .createNewRegistrationRequestReceived(
                                properties);
            }
            default -> {
                content = Content.builder()
                        .header("NO HEADER")
                        .body("NO BODY")
                        .build();
            }
        }
        return content;
    }
}
