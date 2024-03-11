package com.ryazancev.common.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.notification.enums.NotificationStatus;
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
@Schema(description = "Notification DTO")
public class NotificationDto {

    private String id;

    private ContentDto content;

    private Element sender;

    @DateTimeFormat(
            iso = DateTimeFormat.ISO.TIME
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime receivedAt;

    private NotificationStatus status;
}
