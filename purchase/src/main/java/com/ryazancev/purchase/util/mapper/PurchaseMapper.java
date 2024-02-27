package com.ryazancev.purchase.util.mapper;

import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.purchase.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PurchaseMapper {

    Purchase toEntity(PurchaseEditDto purchaseEditDto);

    List<PurchaseDto> toListDto(List<Purchase> purchase);

    PurchaseDto toDto(Purchase purchase);
}
