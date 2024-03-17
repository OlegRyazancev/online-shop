package com.ryazancev.notification.util.processor;

import com.ryazancev.notification.model.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class ContentProcessor {

    @Value("${spring.mail}")
    private String mail;

    private final MessageSource messageSource;

    public Content createProductRegistrationAcceptedContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.product_registration_accepted.header",
                null,
                Locale.getDefault());

        String body = messageSource.getMessage(
                "content.notification.product_registration_accepted.body",
                new Object[]{
                        properties.getProperty("product_name")
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createProductRegistrationRejectedContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.product_registration_rejected.header",
                null,
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.product_registration_rejected.body",
                new Object[]{
                        properties.getProperty("product_name"),
                        mail
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createOrganizationRegistrationAcceptedContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.organization_registration_accepted.header",
                null,
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.organization_registration_accepted.body",
                new Object[]{
                        properties.getProperty("organization_name")
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createOrganizationRegistrationRejectedContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.organization_registration_rejected.header",
                null,
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.organization_registration_rejected.body",
                new Object[]{
                        properties.getProperty("organization_name"),
                        mail
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createAccountLockedContent() {

        String header = messageSource.getMessage(
                "content.notification.account_locked.header",
                null,
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.account_locked.body",
                new Object[]{
                        mail
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createAccountUnlockedContent() {

        String header = messageSource.getMessage(
                "content.notification.account_unlocked.header",
                null,
                Locale.getDefault()
        );
        String body = messageSource.getMessage(
                "content.notification.account_unlocked.body",
                null,
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createReviewCreatedContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.review_created.header",
                null,
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.review_created.body",
                new Object[]{
                        properties.getProperty("product_rating"),
                        properties.getProperty("product_name")
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createPurchaseProcessedContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.purchase_processed.header",
                null,
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.purchase_processed.body",
                new Object[]{
                        properties.getProperty("product_name")
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createNewProductAvailableContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.new_product_available.header",
                null,
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.new_product_available.body",
                new Object[]{
                        properties.getProperty("product_name"),
                        properties.getProperty("product_price")
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createFreezeObjectContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.object_freeze.header",
                new Object[]{
                        properties.getProperty("object_type")
                },
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.object_freeze.body",
                new Object[]{
                        properties.getProperty("object_name"),
                        mail
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createActivateObjectContent(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.object_activate.header",
                new Object[]{
                        properties.getProperty("object_type")
                },
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.object_activate.body",
                new Object[]{
                        properties.getProperty("object_name")
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    public Content createNewRegistrationRequestReceived(
            Properties properties) {

        String header = messageSource.getMessage(
                "content.notification.new_registration_request_received.header",
                null,
                Locale.getDefault()
        );

        String body = messageSource.getMessage(
                "content.notification.new_registration_request_received.body",
                new Object[]{
                        properties.getProperty("object_type")
                },
                Locale.getDefault()
        );

        return buildContent(header, body);
    }

    private Content buildContent(String header, String body) {

        return Content.builder()
                .header(header)
                .body(body)
                .build();
    }
}
