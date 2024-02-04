package com.ryazancev.admin.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProductRegRequest implements Serializable {

    private Long id;
    private Long productId;

    private RequestStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;
}
