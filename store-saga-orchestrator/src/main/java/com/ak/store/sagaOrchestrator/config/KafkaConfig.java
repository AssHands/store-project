package com.ak.store.sagaOrchestrator.config;

import com.ak.store.sagaOrchestrator.util.saga.SagaProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final KafkaListenerEndpointRegistry registry;
    private final ConcurrentKafkaListenerContainerFactory<?, ?> containerFactory;
    private final SagaProperties sagaProperties;
    private final ApplicationContext applicationContext;

    @Value("${spring.kafka.consumer.group-id}")
    private String GROUP_ID;

    @PostConstruct
    public void registerSagaListeners() {
        sagaProperties.getSagaDefinitions().forEach((sagaKey, sagaDef) -> {
            List<String> topics = new ArrayList<>();
            topics.add(sagaDef.getTopic());
            topics.addAll(sagaDef.getSteps().stream().map(SagaProperties.SagaStep::getTopic).toList());

            Object handlerBean = applicationContext.getBean(sagaDef.getName());
            MethodKafkaListenerEndpoint<Object, Object> endpoint = new MethodKafkaListenerEndpoint<>();
            endpoint.setId("saga-listener-" + sagaKey);
            endpoint.setGroupId(GROUP_ID);
            endpoint.setTopics(topics.toArray(new String[0]));
            endpoint.setBean(handlerBean);
            endpoint.setMethod(ReflectionUtils.findMethod(handlerBean.getClass(), "handle", List.class, Acknowledgment.class));
            endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());

            registry.registerListenerContainer(endpoint, containerFactory, true);
        });
    }
}