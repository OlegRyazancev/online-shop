package com.ryazancev.common.clients;

import com.ryazancev.common.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Oleg Ryazancev
 */

@FeignClient(
        name = "logo",
        configuration = FeignClientsConfiguration.class,
        url = "${clients.logo.url}"
)
public interface LogoClient {

    @PostMapping(value = "api/v1/logos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(
            @RequestPart("file") MultipartFile file);
}
