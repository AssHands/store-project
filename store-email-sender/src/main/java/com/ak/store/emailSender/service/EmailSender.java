package com.ak.store.emailSender.service;

import com.ak.store.common.event.order.OrderCreatedEvent;
import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.order.dto.OrderProductDTO;
import com.ak.store.emailSender.feign.CatalogueFeign;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class EmailSender {
    private final JavaMailSender mailSender;
    private final CatalogueFeign catalogueFeign;
    private final String VERIFY_URL = "http://localhost:8081/api/v1/consumer/consumers/verify?code=%s";
    private final String VERIFY_SUBJECT = "Please verify your registration";
    private final String VERIFY_CONTENT = """
            Please click the link below to verify your registration:<br>
            <h3><a href="%s" target="_self">VERIFY</a></h3>
            Thank you,<br>
            Store.
            """;
    private final String ORDER_SUBJECT = "Your order created";
    private final String ORDER_CONTENT = """
            Your order:<br>
            <h2>%s</h2>
            Total price: %d$<br>
            Thank you,<br>
            Store.
            """;

    public void sendVerificationEmail(String email, String verificationCode) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setSubject(VERIFY_SUBJECT);

            String url = VERIFY_URL.formatted(verificationCode);
            String content = VERIFY_CONTENT.formatted(url);
            helper.setText(content, true);
        } catch (Exception e) {
            throw new RuntimeException("error while sending email");
        }

        mailSender.send(message);
    }

    public void sendOrderCreatedNotification(OrderCreatedEvent orderCreatedEvent) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(orderCreatedEvent.getConsumerEmail());
            helper.setSubject(ORDER_SUBJECT);

            String content = ORDER_CONTENT.formatted(
                    getOrderContent(orderCreatedEvent), orderCreatedEvent.getTotalPrice());

            helper.setText(content, true);
        } catch (Exception e) {
            throw new RuntimeException("error while sending email");
        }

        mailSender.send(message);
    }

    private String getOrderContent(OrderCreatedEvent orderCreatedEvent) {
        List<Long> ids = orderCreatedEvent.getOrderProducts().stream().map(OrderProductDTO::getProductId).toList();
        List<ProductPoorView> products = catalogueFeign.findAllProductPoor(ids);
        Map<Integer, ProductPoorView> orderContent = new HashMap<>();

        for (var orderProduct : orderCreatedEvent.getOrderProducts()) {
            orderContent.put(
                    orderProduct.getAmount(),
                    products.stream()
                            .filter(p -> p.getId().equals(orderProduct.getProductId()))
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
