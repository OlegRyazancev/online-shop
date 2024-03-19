package com.ryazancev.mail.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@OpenAPIDefinition
@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final OpenApiProperties properties;

    @Bean
    public OpenAPI customOpenApi(
            @Value("${openapi.service.url}") final String url) {

        return new OpenAPI()
                .servers(List.of(new Server()
                        .url(url)))
                .info(new Info()
                        .title(properties.getTitle())
                        .description(properties.getDescription())
                        .version(properties.getVersion())
                        .contact(new Contact()
                                .name(properties.getContact().getName())
                                .email(properties.getContact().getEmail())));
    }
}
