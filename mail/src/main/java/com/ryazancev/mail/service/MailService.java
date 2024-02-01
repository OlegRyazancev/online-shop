package com.ryazancev.mail.service;

import com.ryazancev.dto.MailDTO;


public interface MailService {
    void sendEmail(MailDTO mailDTO);
}
