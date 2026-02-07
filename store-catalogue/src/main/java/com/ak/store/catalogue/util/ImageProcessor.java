package com.ak.store.catalogue.util;

import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.command.WriteImageCommand;
import com.ak.store.catalogue.model.pojo.ProcessedImages;
import com.ak.store.catalogue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ImageProcessor {
    private final ProductService productService;

    public ProcessedImages process(WriteImageCommand request, List<ImageDTO> images) {
        ProcessedImages processedImages = new ProcessedImages();

        processedImages.setImageKeysForDelete(
                markImagesForDeleteAndGetKeys(images, request.getDeleteImageIndexes()));

        LinkedHashMap<String, MultipartFile> imagesForAdd =
                prepareImagesForAdd(request.getProductId(), request.getAddImages());

        for (String key : imagesForAdd.keySet()) {
            images.add(ImageDTO.builder()
                    .key(key)
                    .build());
        }
        processedImages.setImagesForAdd(imagesForAdd);

        processedImages.setAllImages(
                createNewImages(images, request.getAllImageIndexes()));

        return processedImages;
    }

    private List<String> markImagesForDeleteAndGetKeys(List<ImageDTO> images, List<String> deleteImageIndexes) {
        List<String> imageKeysForDelete = new ArrayList<>();
        if (deleteImageIndexes != null && !deleteImageIndexes.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                var index = images.get(i).getIndex();
                boolean isDeleted = deleteImageIndexes.stream()
                        .map(Integer::parseInt)
                        .anyMatch(deletedIndex -> deletedIndex.equals(index));

                if (isDeleted) {
                    imageKeysForDelete.add(images.get(i).getKey());
                    images.set(i, null);
                }
            }
        }
        return imageKeysForDelete;
    }

    private LinkedHashMap<String, MultipartFile> prepareImagesForAdd(Long productId, List<MultipartFile> addImages) {
        //LinkedHashMap для сохранения порядка
        LinkedHashMap<String, MultipartFile> imagesForAdd = new LinkedHashMap<>();
        if (addImages != null && !addImages.isEmpty()) {
            imagesForAdd = generateImageKeys(productId, addImages);
        }
        return imagesForAdd;
    }

    //LinkedHashMap для сохранения порядка
    private LinkedHashMap<String, MultipartFile> generateImageKeys(Long productId, List<MultipartFile> images) {
        LinkedHashMap<String, MultipartFile> imageKeys = new LinkedHashMap<>();
        var product = productService.findOne(productId);
        for(var image : images) {
            imageKeys.put("category_%s/product_%s/%s".formatted(product.getCategoryId(),
                    product.getId(), UUID.randomUUID() + "." + image.getContentType().split("/")[1]), image);
        }

        return imageKeys;
    }

    private List<ImageDTO> createNewImages(List<ImageDTO> images, Map<String, String> allImageIndexes) {
        int finalProductImagesSize = (int) images.stream()
                .filter(Objects::nonNull)
                .count();

        List<ImageDTO> newImages = new ArrayList<>(finalProductImagesSize);
        for (int i = 0; i < finalProductImagesSize; i++) {
            newImages.add(null);
        }

        for (var entry : allImageIndexes.entrySet()) {
            if (!Pattern.compile("image\\[\\d]").matcher(entry.getKey()).matches())
                continue;

            int currentIndex = Integer.parseInt(entry.getKey().replaceAll("\\D", ""));
            int newIndex = Integer.parseInt(entry.getValue());

            newImages.set(newIndex, images.get(currentIndex));
            newImages.get(newIndex).setIndex(newIndex);
        }

        return newImages;
    }
}
