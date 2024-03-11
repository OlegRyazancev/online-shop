package com.ryazancev.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Oleg Ryazancev
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Content implements Serializable {


    private String header;

    private String body;
}
