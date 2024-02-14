package com.ryazancev.organization.util.validator;

import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class OrganizationStatusValidator {

    public void validateFrozenStatus(Organization organization) {

        if (organization.getStatus().equals(OrganizationStatus.FROZEN)) {

            throw new AccessDeniedException(
                    "Access denied. Organization is frozen",
                    HttpStatus.CONFLICT);
        }
    }

    public void validateInactiveStatus(Organization organization) {

        if (organization.getStatus().equals(OrganizationStatus.INACTIVE)) {

            throw new AccessDeniedException(
                    "Access denied. Organization is inactive",
                    HttpStatus.CONFLICT);
        }
    }

    public void validateDeletedStatus(Organization organization) {

        if (organization.getStatus().equals(OrganizationStatus.DELETED)) {

            throw new AccessDeniedException(
                    "Access denied. Organization is deleted",
                    HttpStatus.CONFLICT);
        }
    }

    public void validateAllStatus(Organization organization) {

        validateDeletedStatus(organization);
        validateInactiveStatus(organization);
        validateFrozenStatus(organization);
    }
}
