package com.ryazancev.logo.config;

import com.ryazancev.logo.util.exception.CustomErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oleg Ryazancev
 */

@Configuration
public class CustomErrorCodeConfig {

    @Autowired
    public CustomErrorCodeConfig(final MessageSource messageSource) {
        CustomErrorCode.setMessageSource(messageSource);
    }
}
