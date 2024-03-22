package com.ryazancev.review.util.exception;

import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.review.util.exception.custom.ReviewCreationException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static ReviewCreationException getReviewCreation() {

        return new ReviewCreationException();
    }

    public static ServiceUnavailableException getServiceUnavailable() {

        return new ServiceUnavailableException();
    }
}
