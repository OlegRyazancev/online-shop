package com.ryazancev.organization.util.validator;

import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
import com.ryazancev.organization.util.exception.custom.OrganizationCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.ryazancev.organization.util.exception.Message.*;

@Component
@RequiredArgsConstructor
public class OrganizationValidator {

    private final OrganizationRepository organizationRepository;

    public void validateFrozenStatus(Organization organization) {

        if (organization.getStatus().equals(OrganizationStatus.FROZEN)) {

            throw new AccessDeniedException(
                    ORGANIZATION_FROZEN,
                    HttpStatus.CONFLICT);
        }
    }

    public void validateInactiveStatus(Organization organization) {

        if (organization.getStatus().equals(OrganizationStatus.INACTIVE)) {

            throw new AccessDeniedException(
                    ORGANIZATION_INACTIVE,
                    HttpStatus.CONFLICT);
        }
    }

    public void validateDeletedStatus(Organization organization) {

        if (organization.getStatus().equals(OrganizationStatus.DELETED)) {

            throw new AccessDeniedException(
                    ORGANIZATION_DELETED,
                    HttpStatus.CONFLICT);
        }
    }

    public void validateAllStatus(Organization organization) {

        validateDeletedStatus(organization);
        validateInactiveStatus(organization);
        validateFrozenStatus(organization);
    }

    public void validateNameUniqueness(Organization organization) {

        if (organizationRepository
                .findByName(organization.getName())
                .isPresent()) {

            throw new OrganizationCreationException(
                    NAME_EXISTS,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateDescriptionUniqueness(Organization organization) {

        if (organizationRepository
                .findByDescription(organization.getDescription())
                .isPresent()) {

            throw new OrganizationCreationException(
                    DESCRIPTION_EXISTS,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
