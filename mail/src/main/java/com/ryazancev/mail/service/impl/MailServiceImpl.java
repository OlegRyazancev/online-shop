package com.ryazancev.mail.service.impl;

import com.ryazancev.dto.mail.MailDto;
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
    public void sendEmail(MailDto mailDto) {
        switch (mailDto.getType()) {
            case REGISTRATION -> sendRegistrationEmail(mailDto);
            case CONFIRMATION -> sendConfirmationEmail(mailDto);
            default -> {
            }
        }
    }

    @SneakyThrows
    private void sendRegistrationEmail(MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        false,
                        "UTF-8"
                );
        helper.setSubject(
                "Thank you for registration, "
                        + mailDto.getName()
                        + "!");
        helper.setTo(mailDto.getEmail());
        String emailContent = getRegistrationEmailContent(mailDto.getName());
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    @SneakyThrows
    private void sendConfirmationEmail(MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        false,
                        "UTF-8"
                );
        helper.setSubject("Please, confirm your email address");
        helper.setTo(mailDto.getEmail());

        String emailContent = getConfirmationContent(
                mailDto.getName(),
                mailDto.getProperties()
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
