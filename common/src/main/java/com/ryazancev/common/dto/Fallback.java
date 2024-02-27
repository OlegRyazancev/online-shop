package com.ryazancev.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Fallback class for response if " +
        "some service unavailable for simple GET requests")
public class Fallback implements Element {

    @Schema(
            description = "Message about status of some service",
            example = "Customer/product... service is not available"
    )
    private String message;
}
