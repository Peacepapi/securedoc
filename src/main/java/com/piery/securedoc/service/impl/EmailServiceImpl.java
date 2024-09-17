package com.piery.securedoc.service.impl;

import com.piery.securedoc.exception.ApiException;
import com.piery.securedoc.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.piery.securedoc.constant.Constants.NEW_USER_ACCOUNT_VERIFICATION;
import static com.piery.securedoc.constant.Constants.PASSWORD_RESET_REQUEST;
import static com.piery.securedoc.utils.EmailUtils.getEmailMessage;
import static com.piery.securedoc.utils.EmailUtils.getPasswordResetMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender sender;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMessage(name, host, token));
            sender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Unable to send email");
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getPasswordResetMessage(name, host, token));
            sender.send(message);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Unable to send email");
        }
    }
}
