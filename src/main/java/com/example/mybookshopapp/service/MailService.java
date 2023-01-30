package com.example.mybookshopapp.service;

import com.example.mybookshopapp.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MailService {

    private final Generator generator;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${mail.mail}")
    private String mail;

    @Autowired
    public MailService(Generator generator, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.generator = generator;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public String sendMail(String contact) {
        String code = generator.getSecretCode();
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
        context.setVariables(Map.of("code", code));
        String emailContent = templateEngine.process("mail", context);
        try {
            mimeMessageHelper.setTo(mail);
            mimeMessageHelper.setSubject("Bookstore email verification!");
            mimeMessageHelper.setFrom(mail);
            mimeMessageHelper.setText(emailContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(message);
        return code;
    }
}
