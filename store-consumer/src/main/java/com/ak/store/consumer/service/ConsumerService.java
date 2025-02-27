package com.ak.store.consumer.service;

import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.repository.ConsumerRepo;
import com.ak.store.consumer.util.ConsumerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final ConsumerRepo consumerRepo;
    private final ConsumerMapper consumerMapper;

    private UUID makeUUID(String id) {
        return UUID.fromString(id);
    }

    public Consumer createOne(String id, ConsumerDTO consumerDTO) {
        Consumer consumer = consumerMapper.mapToConsumer(consumerDTO);
        consumer.setId(makeUUID(id));
        return consumerRepo.save(consumer);
    }

    public Consumer findOne(String id) {
        return consumerRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("no consumers found"));
    }

    public void deleteOne(String id) {
        consumerRepo.deleteById(makeUUID(id));
    }

    public Consumer updateOne(String id, ConsumerDTO consumerDTO) {
        Consumer consumer = findOne(id);
        updateConsumer(consumer, consumerDTO);
        return consumerRepo.save(consumer);
    }

    private void updateConsumer(Consumer consumer, ConsumerDTO consumerDTO) {
        if(consumerDTO.getName() != null) {
            consumer.setName(consumerDTO.getName());
        }
        if(consumerDTO.getEmail() != null) {
            consumer.setEmail(consumerDTO.getEmail());
        }
        if(consumerDTO.getPassword() != null) {
            consumer.setPassword(consumerDTO.getPassword());
        }
    }

    public Boolean existOne(String id) {
        return consumerRepo.existsOneById(makeUUID(id));
    }
}
