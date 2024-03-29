package com.ryazancev.mail.service.impl;

import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.mail.service.MailService;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Override
    public void sendEmail(
            final MailDto mailDto) {

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

                log.warn("Unknown mail type: {}", mailDto.getType());
            }
        }
    }

    @SneakyThrows
    private void sendRejectedObjectRegistrationEmail(
            final MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject(
                messageSource.getMessage(
                        "service.mail.rejected_registration_subject",
                        new Object[]{mailDto.getName()},
                        Locale.getDefault()
                )
        );
        helper.setTo(mailDto.getEmail());

        String emailContent = getObjectRegistrationEmailContent(
                mailDto.getName(),
                mailDto.getProperties(),
                "rejected_registration.ftlh");

        helper.setText(emailContent, true);

        mailSender.send(message);

    }

    @SneakyThrows
    private void sendAcceptedObjectRegistrationEmail(
            final MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject(
                messageSource.getMessage(
                        "service.mail.accepted_registration_subject",
                        new Object[]{mailDto.getName()},
                        Locale.getDefault()
                )
        );
        helper.setTo(mailDto.getEmail());

        String emailContent = getObjectRegistrationEmailContent(
                mailDto.getName(),
                mailDto.getProperties(),
                "accepted_registration.ftlh");

        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    @SneakyThrows
    private void sendRegistrationEmail(
            final MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, false, "UTF-8");
        helper.setSubject(
                messageSource.getMessage(
                        "service.mail.registration_welcome_subject",
                        new Object[]{mailDto.getName()},
                        Locale.getDefault()
                )
        );
        helper.setTo(mailDto.getEmail());

        String emailContent = getRegistrationEmailContent(mailDto.getName());
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    @SneakyThrows
    private void sendConfirmationEmail(
            final MailDto mailDto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        false,
                        "UTF-8"
                );
        helper.setSubject(
                messageSource.getMessage(
                        "service.mail.confirm_mail_subject",
                        null,
                        Locale.getDefault()
                )
        );
        helper.setTo(mailDto.getEmail());

        String emailContent = getConfirmationContent(
                mailDto.getName(),
                mailDto.getProperties()
        );
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(
            final String name) {

        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("name", name);

        configuration.getTemplate("register.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getConfirmationContent(final String name,
                                          final Properties properties) {

        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("name", name);
        model.put("link", properties.getProperty("link"));

        configuration.getTemplate("confirm.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();
    }

    @SneakyThrows
    private String getObjectRegistrationEmailContent(
            final String name,
            final Properties properties,
            final String template) {

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
