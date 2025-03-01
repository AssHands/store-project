package com.ak.store.consumer.service;

import com.ak.store.consumer.model.entity.Consumer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class EmailSender {
    private final JavaMailSender mailSender;
    private final String VERIFY_URL = "http://localhost:8081/api/v1/consumer/consumers/verify?code=%s";
    private final String SUBJECT = "Please verify your registration";
    private final String CONTENT = """
            Dear %s,<br>
            Please click the link below to verify your registration:<br>
            <h3><a href="%s" target="_self">VERIFY</a></h3>
            Thank you,<br>
            Store.
            """;

    public void sendVerificationEmail(Consumer consumer, String code) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(consumer.getEmail());
            helper.setSubject(SUBJECT);

            String url = VERIFY_URL.formatted(code);
            String content = CONTENT.formatted(consumer.getName(), url);
            helper.setText(content, true);
        } catch (Exception e) {
            throw new RuntimeException("error while sending email");
        }

        mailSender.send(message);
    }
}
