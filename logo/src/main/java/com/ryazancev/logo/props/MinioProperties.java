package com.ryazancev.logo.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String bucket;
    private String url;
    private String accessKey;
    private String secretKey;
}
