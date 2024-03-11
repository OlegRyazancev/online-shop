package com.ryazancev.notification.service.impl;

import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.notification.model.Content;
import com.ryazancev.notification.service.ContentService;
import com.ryazancev.notification.util.ContentUtil;
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

    private final ContentUtil contentUtil;

    @Override
    public Content generateContent(NotificationType type, Properties properties) {
        Content content;

        switch (type) {
            case PRIVATE_PRODUCT_REGISTRATION_ACCEPTED -> {
                content = contentUtil
                        .createProductRegistrationAcceptedContent(
                                properties);
            }
            case PRIVATE_PRODUCT_REGISTRATION_REJECTED -> {
                content = contentUtil
                        .createProductRegistrationRejectedContent(
                                properties);
            }
            case PRIVATE_ORGANIZATION_REGISTRATION_ACCEPTED -> {
                content = contentUtil
                        .createOrganizationRegistrationAcceptedContent(
                                properties);
            }
            case PRIVATE_ORGANIZATION_REGISTRATION_REJECTED -> {
                content = contentUtil
                        .createOrganizationRegistrationRejectedContent(
                                properties);
            }
            case PRIVATE_ACCOUNT_LOCKED -> {
                content = contentUtil
                        .createAccountLockedContent();
            }
            case PRIVATE_ACCOUNT_UNLOCKED -> {
                content = contentUtil
                        .createAccountUnlockedContent();
            }
            case PRIVATE_REVIEW_RECEIVED -> {
                content = contentUtil
                        .createReviewReceivedContent(
                                properties);
            }
            case PRIVATE_PURCHASE_PROCESSED -> {
                content = contentUtil
                        .createPurchaseProcessedContent(
                                properties);
            }
            case PUBLIC_NEW_PRODUCT_CREATED -> {
                content = contentUtil
                        .createNewProductAvailableContent(
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
