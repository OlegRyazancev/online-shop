package com.ryazancev.purchase.util.mappers;

import com.ryazancev.clients.PurchaseDTO;
import com.ryazancev.purchase.model.Purchase;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    Purchase toEntity(PurchaseDTO purchaseDTO);

    List<PurchaseDTO> toDTO(List<Purchase> purchase);
}
