package com.ak.store.paymentSagaWorker.service;

import com.ak.store.paymentSagaWorker.mapper.OutboxEventMapper;
import com.ak.store.paymentSagaWorker.repository.OutboxEventRepo;
import com.ak.store.paymentSagaWorker.model.entity.OutboxEventStatus;
import com.ak.store.paymentSagaWorker.model.entity.OutboxEventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OutboxEventService {
    private final OutboxEventRepo outboxEventRepo;
    private final OutboxEventMapper outboxEventMapper;

    @Transactional
    public <T> void createOne(UUID id, T payload, OutboxEventType type) {
        var event = outboxEventMapper.toOutboxEvent(payload);

        //todo если такое id уже есть в БД - что произойдет?
        // он удалит текущую запись и создаст новую запись с таким же id
        event.setId(id);
        event.setType(type);
        event.setStatus(OutboxEventStatus.IN_PROGRESS);
        event.setRetryTime(LocalDateTime.now());

        outboxEventRepo.save(event);
    }
}