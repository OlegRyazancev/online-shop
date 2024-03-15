package com.ryazancev.organization.util.validator;

import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
import com.ryazancev.organization.util.exception.custom.OrganizationCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;


/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class OrganizationValidator {

    private final OrganizationRepository organizationRepository;

    private final MessageSource messageSource;

    public void validateStatus(Organization organization,
                               OrganizationStatus status) {
        if (organization.getStatus() == status) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.organization.status_access",
                            new Object[]{organization.getStatus()},
                            Locale.getDefault()
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
                    messageSource.getMessage(
                            "exception.organization.name_exists",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateDescriptionUniqueness(Organization organization) {

        if (organizationRepository
                .findByDescription(organization.getDescription())
                .isPresent()) {

            throw new OrganizationCreationException(
                    messageSource.getMessage(
                            "exception.organization.description_exists",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
