package com.ryazancev.organization.util.validator;

import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.util.exception.CustomExceptionFactory;
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

    public void validateStatus(final Organization organization,
                               final OrganizationStatus status) {

        if (organization.getStatus() == status) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .statusAccess(
                            messageSource,
                            organization.getStatus()
                    );
        }
    }


    public void validateAllStatus(
            final Organization organization) {

        validateStatus(organization, OrganizationStatus.DELETED);
        validateStatus(organization, OrganizationStatus.INACTIVE);
        validateStatus(organization, OrganizationStatus.FROZEN);
    }

    public void validateNameUniqueness(
            final Organization organization) {

        if (organizationRepository
                .findByName(organization.getName())
                .isPresent()) {

            throw CustomExceptionFactory
                    .getOrganizationCreation()
                    .nameExists(messageSource);
        }
    }

    public void validateDescriptionUniqueness(
            final Organization organization) {

        if (organizationRepository
                .findByDescription(organization.getDescription())
                .isPresent()) {

            throw CustomExceptionFactory
                    .getOrganizationCreation()
                    .descriptionExists(messageSource);
        }
    }
}
