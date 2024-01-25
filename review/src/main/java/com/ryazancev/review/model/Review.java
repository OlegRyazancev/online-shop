package com.ryazancev.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "review")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review implements Serializable {

    @Id
    private String id;

    private String body;

    private Long productId;

    private Long customerId;

    private Integer rating;

    private LocalDateTime createdAt;
}
