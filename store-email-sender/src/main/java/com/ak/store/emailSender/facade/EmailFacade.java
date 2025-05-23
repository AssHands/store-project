package com.ak.store.emailSender.facade;

import com.ak.store.emailSender.model.dto.OrderCreatedWriteDTO;
import com.ak.store.emailSender.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailFacade {
    private final EmailService emailService;

    public void sendVerification(String email, String verificationCode) {
        emailService.sendVerification(email, verificationCode);
    }

    public void sendOrderCreated(OrderCreatedWriteDTO request) {
        emailService.sendOrderCreated(request);
    }
}
