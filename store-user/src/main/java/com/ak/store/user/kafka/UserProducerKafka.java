package com.ak.store.user.kafka;

import com.ak.store.common.event.user.UserVerifyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class UserProducerKafka {
//    private final KafkaTemplate<String, ConsumerEvent> kafkaProductTemplate;
//
//    public void send(UserVerifyEvent userVerifyEvent) {
//        try {
//            SendResult<String, ConsumerEvent> future = kafkaProductTemplate
//                    .send("consumer-verify-events", userVerifyEvent).get();
//        } catch (Exception e) {
//            throw new RuntimeException("kafka consumer-verify-events error");
//        }
//    }
//}