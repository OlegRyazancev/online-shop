package com.ryazancev.common.dto;

import com.ryazancev.common.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;

public interface Element {

    default <T> T safelyCast(Class<T> targetType) {

        if (this instanceof Fallback) {
            throw new ServiceUnavailableException(
                    ((Fallback) this).getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        return targetType.cast(this);
    }
}
