package com.ryazancev.mail.service;

import com.ryazancev.dto.mail.MailDTO;


public interface MailService {
    void sendEmail(MailDTO mailDTO);
}
