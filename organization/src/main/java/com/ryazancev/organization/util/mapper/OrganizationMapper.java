package com.ryazancev.organization.util.mapper;

import com.ryazancev.dto.organization.OrganizationDto;
import com.ryazancev.dto.organization.OrganizationEditDto;
import com.ryazancev.organization.model.Organization;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrganizationMapper {

    OrganizationDto toDetailedDto(Organization organization);


    @Named("toSimpleDto")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    OrganizationDto toSimpleDto(Organization organization);

    @IterableMapping(qualifiedByName = "toSimpleDto")
    List<OrganizationDto> toSimpleListDto(
            List<Organization> organization);

    Organization toEntity(OrganizationEditDto organizationEditDto);
}
