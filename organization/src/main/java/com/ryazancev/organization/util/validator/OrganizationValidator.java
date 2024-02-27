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

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class OrganizationValidator {

    private final OrganizationRepository organizationRepository;

    public void validateStatus(Organization organization,
                               OrganizationStatus status) {
        if (organization.getStatus().equals(status)) {

            throw new AccessDeniedException(
                    String.format(
                            ORGANIZATION_STATUS_ACCESS,
                            organization.getStatus().name()
                    ),
                    HttpStatus.CONFLICT);
        }
    }


    public void validateAllStatus(Organization organization) {

        validateStatus(organization, OrganizationStatus.DELETED);
        validateStatus(organization, OrganizationStatus.INACTIVE);
        validateStatus(organization, OrganizationStatus.FROZEN);
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
