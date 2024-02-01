package com.ryazancev.purchase.util.mapper;

import com.ryazancev.dto.PurchaseDTO;
import com.ryazancev.dto.PurchaseEditDTO;
import com.ryazancev.purchase.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PurchaseMapper {

    Purchase toEntity(PurchaseEditDTO purchaseEditDTO);

    List<PurchaseDTO> toListDTO(List<Purchase> purchase);

    PurchaseDTO toDTO(Purchase purchase);
}
