package com.ryazancev.mail.service;

import com.ryazancev.clients.mail.dto.MailDTO;


public interface MailService {
    void sendEmail(MailDTO mailDTO);
}
