package com.ryazancev.logo.util.mapper;

import com.ryazancev.clients.logo.LogoDTO;
import com.ryazancev.logo.model.Logo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LogoMapper {
    Logo toEntity(LogoDTO logoDTO);

}
