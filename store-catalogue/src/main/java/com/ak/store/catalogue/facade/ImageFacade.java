package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.command.WriteImageCommand;
import com.ak.store.catalogue.service.ImageService;
import com.ak.store.catalogue.service.outbox.ProductOutboxService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageFacade {
    private final ImageService imageService;
    private final ProductOutboxService productOutboxService;

    @Transactional
    public Long updateAllImage(WriteImageCommand command) {
        imageService.updateAllImage(command);
        productOutboxService.saveUpdatedEvent(command.getProductId());
        return command.getProductId();
    }
}
