package com.ak.store.consumer.facade;

import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.consumer.dto.ConsumerDTO;
import com.ak.store.consumer.feign.CatalogueFeign;
import com.ak.store.consumer.service.ConsumerService;
import com.ak.store.consumer.util.ConsumerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConsumerServiceFacade {
    private final ConsumerService consumerService;
    private final ConsumerMapper consumerMapper;

    @Transactional
    public Long createOne(ConsumerDTO consumerDTO) {
        return consumerService.createOne(consumerDTO).getId();
    }

    public ConsumerDTO findOne(Long id) {
        return consumerMapper.mapToConsumerDTO(consumerService.findOne(id));
    }

    @Transactional
    //todo: не удлаять его отзывы.
    public void deleteOne(Long id) {
        consumerService.deleteOne(id);
    }

    @Transactional
    public Long updateOne(Long id, ConsumerDTO consumerDTO) {
        return consumerService.updateOne(id, consumerDTO).getId();
    }

    public Boolean existOne(Long id) {
        return consumerService.existOne(id);
    }
}
