package com.ryazancev.mail.controller;

import com.ryazancev.clients.mail.dto.MailDTO;
import com.ryazancev.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/mails")
@RequiredArgsConstructor
@Validated
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    void sendEmail(@RequestBody
                   @Validated MailDTO mailDTO) {
        mailService.sendEmail(mailDTO);
    }
}
