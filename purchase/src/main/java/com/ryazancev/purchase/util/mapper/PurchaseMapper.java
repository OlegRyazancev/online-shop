package com.ryazancev.purchase.util.mapper;

import com.ryazancev.clients.purchase.dto.PurchaseDetailedDTO;
import com.ryazancev.clients.purchase.dto.PurchasePostDTO;
import com.ryazancev.purchase.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PurchaseMapper {

    Purchase toEntity(PurchasePostDTO purchasePostDTO);

    List<PurchaseDetailedDTO> toDetailedListDTO(List<Purchase> purchase);

    PurchaseDetailedDTO toDetailedDTO(Purchase purchase);
}
