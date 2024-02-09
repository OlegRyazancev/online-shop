package com.ryazancev.organization.util.mapper;

import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.organization.OrganizationEditDTO;
import com.ryazancev.organization.model.Organization;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrganizationMapper {

    OrganizationDTO toDetailedDTO(Organization organization);


    @Named("toSimpleDTO")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    OrganizationDTO toSimpleDTO(Organization organization);

    @IterableMapping(qualifiedByName = "toSimpleDTO")
    List<OrganizationDTO> toSimpleListDTO(
            List<Organization> organization);

    Organization toEntity(OrganizationEditDTO organizationEditDTO);
}
