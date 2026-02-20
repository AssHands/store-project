package com.ak.store.catalogue.product.facade;

import com.ak.store.catalogue.model.command.WriteProductCommand;
import com.ak.store.catalogue.model.dto.ProductDTO;
import com.ak.store.catalogue.product.service.ProductService;
import com.ak.store.catalogue.service.outbox.ProductOutboxService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductService productService;
    private final ProductOutboxService productOutboxService;

    public ProductDTO findOne(Long id) {
        return productService.findOne(id);
    }

    public List<ProductDTO> findAll(List<Long> ids) {
        return productService.findAll(ids);
    }

    public Boolean isExistAll(List<Long> ids) {
        return productService.isExistAll(ids);
    }

    public Boolean isAvailableAll(List<Long> ids) {
        return productService.isAvailableAll(ids);
    }

    @Transactional
    public Long createOne(WriteProductCommand command) {
        var product = productService.createOne(command);
        productOutboxService.saveCreatedEvent(product.getId());
        return product.getId();
    }

    @Transactional
    public Long updateOne(WriteProductCommand command) {
        var product = productService.updateOne(command);
        productOutboxService.saveUpdatedEvent(product.getId());
        return product.getId();
    }

    @Transactional
    public Long deleteOne(Long id) {
        var product = productService.deleteOne(id);
        productOutboxService.saveDeletedEvent(product.getId());
        return id;
    }
}