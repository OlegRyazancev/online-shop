package com.ryazancev.organization.util.mapper;

import com.ryazancev.clients.organization.OrganizationCreateDTO;
import com.ryazancev.clients.organization.OrganizationDetailedDTO;
import com.ryazancev.clients.organization.OrganizationSimpleDTO;
import com.ryazancev.organization.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrganizationMapper {

    List<OrganizationSimpleDTO> toSimpleListDTO(
            List<Organization> organization);

    OrganizationDetailedDTO toDetailedDTO(Organization organization);

    OrganizationSimpleDTO toSimpleDTO(Organization organization);

    @Mappings({
            @Mapping(
                    target = "logo",
                    ignore = true)
    })
    Organization toEntity(OrganizationCreateDTO organizationCreateDTO);
}
