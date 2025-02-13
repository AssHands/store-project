package com.ak.store.consumer.util;

import com.ak.store.common.model.consumer.dto.CartView;
import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.consumer.model.Cart;
import com.ak.store.consumer.model.Consumer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumerMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ConsumerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Consumer mapToConsumer(ConsumerDTO consumerDTO) {
        return modelMapper.map(consumerDTO, Consumer.class);
    }

    public ConsumerDTO mapToConsumerDTO(Consumer consumer) {
        return modelMapper.map(consumer, ConsumerDTO.class);
    }

    //todo: он зачем то создает объект ProductView в CartView, который я затем перезаписываю. исправить
    public CartView mapToCartView(Cart cart) {
        return modelMapper.map(cart, CartView.class);
    }
}
