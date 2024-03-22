package com.ryazancev.organization.util.validator;

import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.util.exception.CustomExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class OrganizationValidator {

    private final OrganizationRepository organizationRepository;

    public void validateStatus(final Organization organization,
                               final OrganizationStatus status) {

        if (organization.getStatus() == status) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .statusAccess(organization.getStatus());
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
                    .nameExists();
        }
    }

    public void validateDescriptionUniqueness(
            final Organization organization) {

        if (organizationRepository
                .findByDescription(organization.getDescription())
                .isPresent()) {

            throw CustomExceptionFactory
                    .getOrganizationCreation()
                    .descriptionExists();
        }
    }
}
