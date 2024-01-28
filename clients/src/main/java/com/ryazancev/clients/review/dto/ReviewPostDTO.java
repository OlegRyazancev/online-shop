package com.ryazancev.clients.review.dto;


import com.ryazancev.validation.OnCreate;
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
public class ReviewPostDTO {

    @NotNull(message = "Body must not be null",
            groups = OnCreate.class)
    @Size(min = 10,
            max = 500,
            message = "Body must be between 10 and 500 characters",
            groups = OnCreate.class)
    private String body;

    @NotNull(message = "Product ID must not be null",
            groups = OnCreate.class)
    private Long productId;

    @NotNull(message = "Customer ID must not be null",
            groups = OnCreate.class)
    private Long customerId;

    @NotNull(message = "Rating must not be null",
            groups = OnCreate.class)
    @Min(value = 1,
            message = "Rating must be at least 1",
            groups = OnCreate.class)
    @Max(value = 5,
            message = "Rating must be at most 5",
            groups = OnCreate.class)
    private Integer rating;
}
