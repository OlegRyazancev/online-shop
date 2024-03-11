package com.ryazancev.notification.service;

import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.notification.model.Content;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

public interface ContentService {

    Content generateContent(NotificationType type, Properties properties);
}
