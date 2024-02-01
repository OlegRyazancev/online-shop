package com.ryazancev.clients.mail;


import com.ryazancev.clients.mail.dto.MailDTO;
import com.ryazancev.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "mail",
        configuration = FeignClientsConfiguration.class
)
public interface MailClient {

    @PostMapping("api/v1/mails/send")
    void sendEmail(@RequestBody
                   @Validated MailDTO mailDTO);
}
