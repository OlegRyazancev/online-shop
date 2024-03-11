package com.ryazancev.common.dto.notification;

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
@Schema(description = "Content DTO. Uses in Notification Dto")
public class ContentDto {

    private String header;
    private String body;
}
