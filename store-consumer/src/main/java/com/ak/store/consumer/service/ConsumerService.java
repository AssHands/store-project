package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.form.ConsumerForm;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.model.entity.VerificationCode;
import com.ak.store.consumer.repository.ConsumerRepo;
import com.ak.store.consumer.util.mapper.ConsumerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final ConsumerRepo consumerRepo;
    private final ConsumerMapper consumerMapper;

    public Consumer createOne(String id, ConsumerForm consumerForm) {
        Consumer consumer = consumerMapper.toConsumer(consumerForm);
        consumer.setId(UUID.fromString(id));
        consumer.setEnabled(false);
        return consumerRepo.save(consumer);
    }

    public Consumer findOne(String id) {
        return consumerRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("no consumers found"));
    }

    public void deleteOne(String id) {
        consumerRepo.deleteById(UUID.fromString(id));
    }

    public Consumer updateOne(String id, ConsumerForm consumerForm) {
        Consumer consumer = findOne(id);
        updateConsumer(consumer, consumerForm);
        return consumerRepo.save(consumer);
    }

    private void updateConsumer(Consumer consumer, ConsumerForm consumerForm) {
        if (consumerForm.getName() != null) {
            consumer.setName(consumerForm.getName());
        }
        if (consumerForm.getPassword() != null) {
            consumer.setPassword(consumerForm.getPassword());
        }
    }

    public Boolean existOne(String id) {
        return consumerRepo.existsOneById(UUID.fromString(id));
    }

    public Consumer verifyOne(String code) {
        Consumer consumer = consumerRepo.findOneByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("No consumer found"));

        if(consumer.getVerificationCode().getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("this verify code is expired");
        }

        consumer.setEnabled(true);
        String email = consumer.getVerificationCode().getEmail();
        consumer.setEmail(email);
        consumer.setVerificationCode(null);

        return consumerRepo.save(consumer);
    }

    public Consumer makeVerificationCode(String id, String verificationCode, String email) {
        Consumer consumer = consumerRepo.findOneWithCodeById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("product no found"));

        consumer.setVerificationCode(VerificationCode.builder()
                .code(verificationCode)
                .consumer(Consumer.builder().id(UUID.fromString(id)).build())
                .expiresAt(LocalDateTime.now().plusDays(1L))
                .email(email)
                .build()
        );

        return consumerRepo.save(consumer);
    }
}
