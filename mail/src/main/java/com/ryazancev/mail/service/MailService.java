package com.ryazancev.mail.service;

import com.ryazancev.dto.mail.MailDto;


public interface MailService {
    void sendEmail(MailDto mailDto);
}
