package com.ryazancev.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicNotification implements Serializable {

    private String id;
    private Content content;
    private LocalDateTime timestamp;

}
