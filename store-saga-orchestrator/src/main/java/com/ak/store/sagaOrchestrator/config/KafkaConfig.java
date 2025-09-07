//package com.ak.store.sagaOrchestrator.config;
//
//import com.ak.store.common.saga.SagaResponseEvent;
//import com.ak.store.sagaOrchestrator.kafka.SagaConsumerKafka;
//import com.ak.store.sagaOrchestrator.util.SagaProperties;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
//import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
//import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
//
//import java.lang.reflect.Method;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Configuration
//public class KafkaConfig {
//    private final KafkaListenerEndpointRegistry registry;
//    private final KafkaListenerContainerFactory<?> containerFactory;
//    private final SagaConsumerKafka handler;
//    private final SagaProperties sagaProperties;
//
//    @Value("${spring.kafka.consumer.group-id}")
//    private String GROUP_ID;
//
//    private static final Method HANDLE_RESPONSE_METHOD;
//    private static final Method HANDLE_COMPENSATION_METHOD;
//    private static final Method HANDLE_COMPLETED_METHOD;
//
//    static {
//        try {
//            HANDLE_RESPONSE_METHOD = SagaConsumerKafka.class.getMethod("handleResponse", List.class, Acknowledgment.class);
//            HANDLE_COMPENSATION_METHOD = SagaConsumerKafka.class.getMethod("handleCompensation", List.class, Acknowledgment.class);
//            HANDLE_COMPLETED_METHOD = SagaConsumerKafka.class.getMethod("handleCompletedSaga", List.class, Acknowledgment.class);
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException("Failed to initialize Kafka listener methods", e);
//        }
//    }
//
//    @PostConstruct
//    public void registerListener() {
//        registerEndpoint("saga-endpoint-response", sagaProperties.getAllResponseStepTopics(), HANDLE_RESPONSE_METHOD);
//        registerEndpoint("saga-endpoint-compensation", sagaProperties.getAllResponseCompensationStepTopics(), HANDLE_COMPENSATION_METHOD);
//        registerEndpoint("saga-endpoint-completed", sagaProperties.getAllResponseSagaTopics(), HANDLE_COMPLETED_METHOD);
//    }
//
//    private void registerEndpoint(String id, List<String> topics, Method method) {
//        MethodKafkaListenerEndpoint<String, SagaResponseEvent> endpoint = new MethodKafkaListenerEndpoint<>();
//
//        endpoint.setId(id);
//        endpoint.setGroupId(GROUP_ID);
//        endpoint.setTopics(topics.toArray(new String[0]));
//        endpoint.setBean(handler);
//        endpoint.setMethod(method);
//        endpoint.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
//
//        registry.registerListenerContainer(endpoint, containerFactory, true);
//    }
//
//    @Bean
//    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
//        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
//        factory.setMessageConverter(new MappingJackson2MessageConverter());
//        return factory;
//    }
//}