package com.ak.store.emailSender.service;

import com.ak.store.emailSender.util.EmailProperties;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    private final EmailProperties emailProperties;

    public void sendVerification(String email, String verificationCode) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setSubject(emailProperties.getVerify().getSubject());

            String url = emailProperties.getVerify().getUrl().formatted(verificationCode);
            String content = emailProperties.getVerify().getContent().formatted(url);
            helper.setText(content, true);
        } catch (Exception e) {
            throw new RuntimeException("error while sending email");
        }

        mailSender.send(message);
    }
}
