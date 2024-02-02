package com.ryazancev.mail.service.impl;

import com.ryazancev.dto.mail.MailDTO;
import com.ryazancev.mail.service.MailService;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(MailDTO mailDTO) {
        switch (mailDTO.getType()) {
            case REGISTRATION -> sendRegistrationEmail(mailDTO);
            case CONFIRMATION -> sendConfirmationEmail(mailDTO);
            default -> {
            }
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(MailDTO mailDTO) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        false,
                        "UTF-8"
                );
        helper.setSubject(
                "Thank you for registration, "
                        + mailDTO.getName()
                        + "!");
        helper.setTo(mailDTO.getEmail());
        String emailContent = getRegistrationEmailContent(mailDTO.getName());
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    @SneakyThrows
    private void sendConfirmationEmail(MailDTO mailDTO) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        false,
                        "UTF-8"
                );
        helper.setSubject("Please, confirm your email address");
        helper.setTo(mailDTO.getEmail());

        String emailContent = getConfirmationContent(
                mailDTO.getName(),
                mailDTO.getProperties()
        );
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(String name) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("name", name);

        configuration.getTemplate("register.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getConfirmationContent(String name,
                                          Properties params) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("name", name);
        model.put("link", params.getProperty("link"));

        configuration.getTemplate("confirm.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }

}
