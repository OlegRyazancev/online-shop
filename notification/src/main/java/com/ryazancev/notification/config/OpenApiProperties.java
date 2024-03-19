package com.ryazancev.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@ConfigurationProperties(prefix = "openapi.info")
@Data
public class OpenApiProperties {

    private String title;
    private String version;
    private String description;
    private Contact contact;

    @Data
    protected static class Contact {
        private String name;
        private String email;
    }
}
