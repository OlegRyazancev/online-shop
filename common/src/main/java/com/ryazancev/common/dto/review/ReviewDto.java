package com.ryazancev.common.dto.review;

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
@Schema(description = "Review DTO. Used in GET methods")
public class ReviewDto {

    @Schema(
            description = "Review ID",
            example = "1"
    )
    private String id;

    @Schema(
            description = "Content of the review",
            example = "Example content of good review"
    )
    private String body;

    @Schema(description = "Product associated with the review")
    private Element product;

    @Schema(description = "Customer who posted the review")
    private Element customer;


    @Schema(description = "Purchase associated with review")
    private Element purchase;

    @Schema(
            description = "Rating given in the review. 1-5",
            example = "5"
    )
    private Integer rating;

    @Schema(
            description = "Date and time when the review was created",
            example = "2024-02-05T22:15:51"
    )
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.TIME
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
