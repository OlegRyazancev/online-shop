package com.ryazancev.mail.service;

import com.ryazancev.common.dto.mail.MailDto;


public interface MailService {
    void sendEmail(MailDto mailDto);
}
