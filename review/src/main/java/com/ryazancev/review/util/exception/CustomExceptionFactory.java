package com.ryazancev.review.util.exception;

import com.ryazancev.review.util.exception.custom.ReviewCreationException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static ReviewCreationException getReviewCreation() {

        return new ReviewCreationException();
    }
}
