package com.ryazancev.auth.util.exception;

import com.ryazancev.auth.util.exception.custom.AccessDeniedException;
import com.ryazancev.auth.util.exception.custom.ConfirmationTokenException;
import com.ryazancev.auth.util.exception.custom.UserCreationException;
import com.ryazancev.auth.util.exception.custom.UserNotFoundException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static AccessDeniedException getAccessDenied() {

        return new AccessDeniedException();
    }

    public static ConfirmationTokenException getConfirmationToken() {

        return new ConfirmationTokenException();
    }

    public static UserCreationException getUserCreation() {

        return new UserCreationException();
    }

    public static UserNotFoundException getUserNotFound() {

        return new UserNotFoundException();
    }
}
