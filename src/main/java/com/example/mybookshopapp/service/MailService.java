package com.example.mybookshopapp.service;

import com.example.mybookshopapp.config.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailConfig emailConfig;

    @Autowired
    public MailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, EmailConfig emailConfig) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.emailConfig = emailConfig;
    }

    @Async
    @Transactional
    public void sendMail(String contact, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        Context context = new Context();
        context.setVariables(Map.of("type", 1, "text", code));
        String emailContent = templateEngine.process("mail", context);
        try {
            mimeMessageHelper.setTo(emailConfig.getMainMail());
            mimeMessageHelper.setSubject("Bookstore email verification!");
            mimeMessageHelper.setFrom(contact);
            mimeMessageHelper.setText(emailContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(message);
    }
}
