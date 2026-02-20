package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.pojo.ProcessedImages;
import com.ak.store.catalogue.repository.ImageFileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageStorageService {
    private final ImageFileRepo imageFileRepo;

    public void sync(ProcessedImages processedImages) {
        try {
            imageFileRepo.addAllImage(processedImages.getImagesForAdd());
            imageFileRepo.deleteAllImage(processedImages.getImageKeysForDelete());
        } catch (Exception e) {
            imageFileRepo.compensateAddAllImage(processedImages.getImagesForAdd().keySet());
            imageFileRepo.compensateDeleteAllImage(processedImages.getImageKeysForDelete());
            throw e;
        }
    }
}
