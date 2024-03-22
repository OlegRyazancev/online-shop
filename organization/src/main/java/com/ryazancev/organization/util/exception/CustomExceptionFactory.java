package com.ryazancev.organization.util.exception;

import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
import com.ryazancev.organization.util.exception.custom.OrganizationCreationException;
import com.ryazancev.organization.util.exception.custom.OrganizationNotFoundException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static AccessDeniedException getAccessDenied() {

        return new AccessDeniedException();
    }

    public static OrganizationCreationException getOrganizationCreation() {

        return new OrganizationCreationException();
    }

    public static OrganizationNotFoundException getOrganizationNotFound() {

        return new OrganizationNotFoundException();
    }

    public static ServiceUnavailableException getServiceUnavailable() {

        return new ServiceUnavailableException();
    }
}
