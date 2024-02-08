package com.ryazancev.dto.review;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewsResponse implements Serializable {

    private List<ReviewDTO> reviews;
}
