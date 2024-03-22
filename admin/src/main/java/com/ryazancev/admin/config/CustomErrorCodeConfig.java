package com.ryazancev.admin.config;

import com.ryazancev.admin.util.exception.CustomErrorCode;
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
