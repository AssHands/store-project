package com.ak.store.emailSender.service;

import com.ak.store.emailSender.feign.CatalogueFeign;
import com.ak.store.emailSender.model.dto.OrderCreatedWriteDTO;
import com.ak.store.emailSender.util.EmailProperties;
import com.ak.store.emailSender.model.view.ProductView;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final CatalogueFeign catalogueFeign;

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

    public void sendOrderCreated(OrderCreatedWriteDTO request) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(request.getUserEmail());
            helper.setSubject(emailProperties.getOrder().getSubject());

            String content = emailProperties.getOrder().getContent()
                    .formatted(request.getOrderId(), getOrderContent(request));

            helper.setText(content, true);
        } catch (Exception e) {
            throw new RuntimeException("error while sending email");
        }

        mailSender.send(message);
    }

    //todo вынести в отдельный класс?
    private String getOrderContent(OrderCreatedWriteDTO request) {
        var ids = new ArrayList<>(request.getProductAmount().keySet());
        var products = catalogueFeign.findAllProduct(ids);
        Map<Integer, ProductView> orderContent = new HashMap<>();

        for (var productAmount : request.getProductAmount().entrySet()) {
            orderContent.put(
                    productAmount.getValue(),
                    products.stream()
                            .filter(v -> v.getId().equals(productAmount.getKey()))
                            .findFirst().orElse(null)
            );
        }

        StringBuilder sb = new StringBuilder();
        for (var entry : orderContent.entrySet()) {
            sb.append(entry.getValue().getTitle()).append(" - ").append(entry.getKey()).append("<br>");
        }

        return sb.toString();
    }
}
