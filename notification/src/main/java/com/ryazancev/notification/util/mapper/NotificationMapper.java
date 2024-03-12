package com.ryazancev.notification.util.mapper;

import com.ryazancev.common.dto.notification.NotificationDto;
import com.ryazancev.notification.model.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationMapper {

    NotificationDto toDto(Notification notification);

    List<NotificationDto> toListDto(List<Notification> notifications);
}
