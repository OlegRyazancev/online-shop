package com.ryazancev.admin.util.mapper;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.dto.admin.RegistrationRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegistrationRequestMapper {

    List<RegistrationRequestDTO> toDtoList(List<RegistrationRequest> requests);

    RegistrationRequestDTO toDto(RegistrationRequest request);

    RegistrationRequest toEntity(RegistrationRequestDTO requestDTO);
}
