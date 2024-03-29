package com.ryazancev.admin.util.mapper;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper {

    List<RegistrationRequestDto> toDtoList(List<RegistrationRequest> requests);

    RegistrationRequestDto toDto(RegistrationRequest request);

    RegistrationRequest toEntity(RegistrationRequestDto requestDto);
}
