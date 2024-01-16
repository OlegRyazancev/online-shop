package com.ryazancev.purchase.util.mappers;

import com.ryazancev.purchase.dto.PurchaseDTO;
import com.ryazancev.purchase.model.Purchase;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    Purchase toEntity(PurchaseDTO purchaseDTO);
}
