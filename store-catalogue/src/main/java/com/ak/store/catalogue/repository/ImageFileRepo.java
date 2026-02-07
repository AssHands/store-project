package com.ak.store.catalogue.repository;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImageFileRepo {
    void addOneImage(MultipartFile image, String imageKey);

    void addAllImage(Map<String, MultipartFile> imagesForAdd);

    void compensateAddAllImage(Iterable<String> imageKeysForCompensate);

    void deleteAllImage(List<String> imageKeysForDelete);

    void compensateDeleteAllImage(List<String> imageKeysForCompensate);
}