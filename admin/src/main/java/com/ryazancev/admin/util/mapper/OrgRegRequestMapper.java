package com.ryazancev.admin.util.mapper;

import com.ryazancev.admin.dto.OrgRegRequestDTO;
import com.ryazancev.admin.model.OrgRegRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrgRegRequestMapper {

    List<OrgRegRequestDTO> toDtoList(List<OrgRegRequest> requests);

    OrgRegRequestDTO toDto(OrgRegRequest request);

    OrgRegRequest toEntity(OrgRegRequestDTO requestDTO);
}
