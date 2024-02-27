package com.ryazancev.mail.service;

import com.ryazancev.common.dto.mail.MailDto;

/**
 * @author Oleg Ryazancev
 */

public interface MailService {
    void sendEmail(MailDto mailDto);
}
