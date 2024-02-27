package com.ryazancev.mail.service.impl;

import com.ryazancev.common.dto.mail.MailDto;
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

            case USER_REGISTRATION -> {

                sendRegistrationEmail(mailDto);
            }

            case MAIL_CONFIRMATION -> {

                sendConfirmationEmail(mailDto);
            }

            case OBJECT_REGISTRATION_ACCEPTED -> {

                sendAcceptedObjectRegistrationEmail(mailDto);
            }
            case OBJECT_REGISTRATION_REJECTED -> {

                sendRejectedObjectRegistrationEmail(mailDto);
            }

            default -> {
            }
        }
    }

    @SneakyThrows
    private void sendRejectedObjectRegistrationEmail(MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject(
                "Dear " + mailDto.getName() + "! Your registration " +
                        "request were rejected!");
        helper.setTo(mailDto.getEmail());

        String emailContent = getObjectRegistrationEmailContent(
                mailDto.getName(),
                mailDto.getProperties(),
                "rejected_registration.ftlh");

        helper.setText(emailContent, true);

        mailSender.send(message);

    }

    @SneakyThrows
    private void sendAcceptedObjectRegistrationEmail(MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject(
                "Dear " + mailDto.getName() + "! Your registration " +
                        "request were accepted!");
        helper.setTo(mailDto.getEmail());

        String emailContent = getObjectRegistrationEmailContent(
                mailDto.getName(),
                mailDto.getProperties(),
                "accepted_registration.ftlh");

        helper.setText(emailContent, true);

        mailSender.send(message);
    }


    @SneakyThrows
    private void sendRegistrationEmail(MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, false, "UTF-8");
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
                                          Properties properties) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("name", name);
        model.put("link", properties.getProperty("link"));

        configuration.getTemplate("confirm.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getObjectRegistrationEmailContent(String name,
                                                     Properties properties,
                                                     String template) {

        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("name", name);
        model.put("object_name", properties.getProperty("object_name"));
        model.put("object_type", properties.getProperty("object_type"));

        configuration.getTemplate(template)
                .process(model, writer);

        return writer.getBuffer().toString();
    }
}
