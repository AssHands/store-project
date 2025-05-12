package com.ak.store.catalogue.util;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.Image;
import com.ak.store.catalogue.model.pojo.ProcessedProductImages;
import com.ak.store.common.model.catalogue.form.ImageForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;

public abstract class ProductImageProcessor {
    public static ProcessedProductImages processProductImages(ImageForm imageForm, Product updatedProduct) {
        ProcessedProductImages processedProductImages = new ProcessedProductImages();
        List<Image> images = updatedProduct.getImages();

        processedProductImages.setImageKeysForDelete(
                markImagesForDeleteAndGetKeys(images, imageForm.getDeleteImageIndexes()));

        LinkedHashMap<String, MultipartFile> imagesForAdd = prepareImagesForAdd(updatedProduct, imageForm.getAddImages());
        for (String key : imagesForAdd.keySet()) {
            images.add(Image.builder()
                    .key(key)
                    .product(Product.builder()
                            .id(imageForm.getProductId())
                            .build())
                    .build());
        }
        processedProductImages.setImagesForAdd(imagesForAdd);

        processedProductImages.setNewImages(
                createNewProductImageList(images, imageForm.getAllImageIndexes()));

        return processedProductImages;
    }

    private static List<String> markImagesForDeleteAndGetKeys(List<Image> images, List<String> deleteImageIndexes) {
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

    private static LinkedHashMap<String, MultipartFile> prepareImagesForAdd(Product updatedProduct, List<MultipartFile> addImages) {
        //LinkedHashMap for save order
        LinkedHashMap<String, MultipartFile> imagesForAdd = new LinkedHashMap<>();
        if (addImages != null && !addImages.isEmpty()) {
            imagesForAdd = generateImageKeys(updatedProduct, addImages);
        }
        return imagesForAdd;
    }

    //LinkedHashMap for save order
    private static LinkedHashMap<String, MultipartFile> generateImageKeys(Product product, List<MultipartFile> images) {
        LinkedHashMap<String, MultipartFile> imageKeys = new LinkedHashMap<>();
        for(var image : images) {
            imageKeys.put("category_%s/product_%s/%s".formatted(product.getCategory().getId(),
                    product.getId(), UUID.randomUUID() + "." + image.getContentType().split("/")[1]), image);
        }

        return imageKeys;
    }

    private static List<Image> createNewProductImageList(List<Image> images, Map<String, String> allImageIndexes) {
        int finalProductImagesSize = (int) images.stream()
                .filter(Objects::nonNull)
                .count();

        List<Image> newImages = new ArrayList<>(finalProductImagesSize);
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
