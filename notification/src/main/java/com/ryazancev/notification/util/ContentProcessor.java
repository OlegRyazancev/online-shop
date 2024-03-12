package com.ryazancev.notification.util;

import com.ryazancev.notification.model.Content;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
public class ContentProcessor {

    private static final String PROPERTIES_FILE =
            "notification_messages.properties";

    @Value("${spring.mail}")
    private String mail;

    public Content createProductRegistrationAcceptedContent(
            Properties properties) {

        String header = properties
                .getProperty("product_registration_accepted.header");

        String body = MessageFormat.format(
                properties
                        .getProperty("product_registration_accepted.body"),
                properties
                        .getProperty("product_name"));

        return buildContent(header, body);
    }

    public Content createProductRegistrationRejectedContent(
            Properties properties) {

        String header = properties
                .getProperty("product_registration_rejected.header");

        String body = MessageFormat.format(
                properties
                        .getProperty("product_registration_rejected.body"),
                properties
                        .getProperty("product_name"),
                mail);

        return buildContent(header, body);
    }

    public Content createOrganizationRegistrationAcceptedContent(
            Properties properties) {

        String header = properties
                .getProperty("organization_registration_accepted.header");

        String body = MessageFormat.format(
                properties
                        .getProperty("organization_registration_accepted.body"),
                properties
                        .getProperty("organization_name")
        );

        return buildContent(header, body);
    }

    public Content createOrganizationRegistrationRejectedContent(
            Properties properties) {

        String header = properties
                .getProperty("organization_registration_rejected.header");
        String body = MessageFormat.format(
                properties
                        .getProperty("organization_registration_rejected.body"),
                properties
                        .getProperty("organization_name"),
                mail
        );

        return buildContent(header, body);
    }

    public Content createAccountLockedContent(Properties properties) {

        String header = properties
                .getProperty("account_locked.header");
        String body = MessageFormat.format(
                properties
                        .getProperty("account_locked.body"),
                mail
        );

        return buildContent(header, body);
    }

    public Content createAccountUnlockedContent(Properties properties) {

        String header = properties
                .getProperty("account_unlocked.header");
        String body = properties
                .getProperty("account_unlocked.body");

        return buildContent(header, body);
    }

    public Content createReviewReceivedContent(
            Properties properties) {

        String header = properties
                .getProperty("review_received.header");
        String body = MessageFormat.format(
                properties
                        .getProperty("review_received.body"),
                properties
                        .getProperty("product_rating"),
                properties
                        .getProperty("product_name")
        );

        return buildContent(header, body);
    }

    public Content createPurchaseProcessedContent(
            Properties properties) {

        String header = properties
                .getProperty("purchase_processed.header");
        String body = MessageFormat.format(
                properties
                        .getProperty("purchase_processed.body"),
                properties
                        .getProperty("product_name")
        );

        return buildContent(header, body);
    }

    public Content createNewProductAvailableContent(
            Properties properties) {

        String header = properties
                .getProperty("new_product_available.header");
        String body = MessageFormat.format(
                properties
                        .getProperty("new_product_available.body"),
                properties
                        .getProperty("product_name"),
                properties
                        .getProperty("product_price")
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
