package com.ryazancev.purchase.util.mappers;

import com.ryazancev.clients.purchase.PurchaseDTO;
import com.ryazancev.purchase.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseMapper {

    Purchase toEntity(PurchaseDTO purchaseDTO);

    List<PurchaseDTO> toDTO(List<Purchase> purchase);
}
