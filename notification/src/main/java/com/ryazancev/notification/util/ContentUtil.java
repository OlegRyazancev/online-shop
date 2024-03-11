package com.ryazancev.notification.util;

import com.ryazancev.notification.model.Content;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
public class ContentUtil {

    @Value("${spring.mail}")
    private String mail;

    public Content createProductRegistrationAcceptedContent(
            Properties properties) {

        String header = "Product Registration Accepted";
        String body = String.format(
                "Dear user, your request to register the new product " +
                        "'%s' has been accepted. The product " +
                        "has been successfully registered.",
                properties.getProperty("product_name")
        );

        return buildContent(header, body);
    }

    public Content createProductRegistrationRejectedContent(
            Properties properties) {

        String header = "Product Registration Rejected";
        String body = String.format(
                "Dear user, your request to register the new product " +
                        "'%s' has been rejected. Please contact " +
                        "%s for further details.",
                properties.getProperty("product_name"),
                mail
        );

        return buildContent(header, body);
    }

    public Content createOrganizationRegistrationAcceptedContent(
            Properties properties) {

        String header = "Organization Registration Accepted";
        String body = String.format(
                "Dear user, your request to register the new organization " +
                        "'%s' has been accepted. The organization " +
                        "has been successfully registered.",
                properties.getProperty("organization_name")
        );

        return buildContent(header, body);
    }

    public Content createOrganizationRegistrationRejectedContent(
            Properties properties) {

        String header = "Organization Registration Rejected";
        String body = String.format(
                "Dear user, your request to register the new organization " +
                        "'%s' has been rejected. Please contact " +
                        "%s for further details.",
                properties.getProperty("organization_name"),
                mail
        );

        return buildContent(header, body);
    }

    public Content createAccountLockedContent() {

        String header = "Account Locked";
        String body = String.format(
                "Dear user, your account has been locked. Please " +
                        "contact %s for further details.",
                mail
        );

        return buildContent(header, body);
    }

    public Content createAccountUnlockedContent() {

        String header = "Account Unlocked";
        String body = "Dear user, your account has been unlocked.";

        return buildContent(header, body);
    }

    public Content createReviewReceivedContent(
            Properties properties) {

        String header = "Review Received";
        String body = String.format(
                "Dear user, you have received a new review with " +
                        "rating '%s' for the product '%s'.",
                properties.getProperty("product_rating"),
                properties.getProperty("product_name")
        );

        return buildContent(header, body);
    }

    public Content createPurchaseProcessedContent(
            Properties properties) {

        String header = "Purchase Processed";
        String body = String.format(
                "Dear user, your purchase for the product " +
                        "'%s' has been processed.",
                properties.getProperty("product_name")
        );

        return buildContent(header, body);
    }

    public Content createNewProductAvailableContent(
            Properties properties) {

        String header = "New Product Available Now";
        String body = String.format(
                "Dear users, a new product '%s' has arrived. Price: %s.",
                properties.getProperty("product_name"),
                properties.getProperty("product_price")
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
