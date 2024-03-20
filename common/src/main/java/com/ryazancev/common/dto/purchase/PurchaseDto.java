package com.ryazancev.common.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.common.dto.Element;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Purchase DTO. Used in GET methods")
public class PurchaseDto implements Element {

    @Schema(
            description = "Purchase ID",
            example = "1"
    )
    private String id;

    @Schema(description = "Customer who made the purchase")
    private Element customer;

    @Schema(description = "Product purchased")
    private Element product;

    @Schema(
            description = "Amount of the purchase",
            example = "1299.90"
    )
    private Double amount;

    @Schema(
            description = "Date and time when the purchase was made",
            example = "2024-07-22T22:15:21"
    )
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.TIME
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime purchaseDate;
}
