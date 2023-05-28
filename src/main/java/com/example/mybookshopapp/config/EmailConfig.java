package com.example.mybookshopapp.config;

import com.example.mybookshopapp.data.entity.config.Email;
import com.example.mybookshopapp.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    private final EmailRepository emailRepository;

    private JavaMailSenderImpl mailSender;

    @Autowired
    public EmailConfig(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        final Email email = emailRepository.findFirstByIsMain(true);
        return getJavaMailSender(email);
    }

    public void reloadMailSender(Email email) {
        getJavaMailSender(email);
    }

    private JavaMailSender getJavaMailSender(Email email) {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(email.getHost());
        mailSender.setUsername(email.getUsername());
        mailSender.setPort(email.getPort());
        mailSender.setPassword(email.getPassword());
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", email.getProtocol());
        properties.put("mail.smtp.auth", email.isSmtpAuth());
        properties.put("mail.smtp.starttls.enable", email.isSmtpStarttlsEnable());
        properties.put("mail.smtp.ssl.enable", email.isSmtpSslEnable());
        properties.put("mail.debug", email.isDebug());
        return mailSender;
    }

    public String getMainMail() {
        return mailSender.getUsername();
    }
}
