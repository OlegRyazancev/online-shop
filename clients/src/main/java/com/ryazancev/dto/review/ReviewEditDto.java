package com.ryazancev.dto.review;


import com.ryazancev.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Review Edit DTO. Used in POST methods")
public class ReviewEditDto {

    @Schema(
            description = "Content of the review",
            example = "Example content of good review"
    )
    @NotNull(
            message = "Body must not be null",
            groups = OnCreate.class
    )
    @Size(
            min = 10,
            max = 500,
            message = "Body must be between 10 and 500 characters",
            groups = OnCreate.class
    )
    private String body;

    @Schema(
            description = "Rating given in the review. 1-5",
            example = "5"
    )
    @NotNull(
            message = "Rating must not be null",
            groups = OnCreate.class
    )
    @Min(
            value = 1,
            message = "Rating must be at least 1",
            groups = OnCreate.class
    )
    @Max(
            value = 5,
            message = "Rating must be at most 5",
            groups = OnCreate.class
    )
    private Integer rating;

    @Schema(
            description = "Purchase ID associated with created review"
    )
    @NotNull(
            message = "Purchase ID must not be null",
            groups = OnCreate.class
    )
    private String purchaseId;
}
