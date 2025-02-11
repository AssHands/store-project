package com.ak.store.catalogue.util;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductImage;
import com.ak.store.catalogue.model.pojo.ProcessedProductImages;
import com.ak.store.common.dto.catalogue.ProductImageWriteDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;

public abstract class ProductImageProcessor {
    public static ProcessedProductImages processProductImages(ProductImageWriteDTO productImageDTO, Product updatedProduct) {
        ProcessedProductImages processedProductImages = new ProcessedProductImages();
        List<ProductImage> productImages = updatedProduct.getImages();

        processedProductImages.setImageKeysForDelete(
                markImagesForDeleteAndGetKeys(productImages, productImageDTO.getDeleteImageIndexes()));

        LinkedHashMap<String, MultipartFile> imagesForAdd = prepareImagesForAdd(updatedProduct, productImageDTO.getAddImages());
        for (String key : imagesForAdd.keySet()) {
            productImages.add(ProductImage.builder()
                    .imageKey(key)
                    .product(Product.builder()
                            .id(productImageDTO.getProductId())
                            .build())
                    .build());
        }
        processedProductImages.setImagesForAdd(imagesForAdd);

        processedProductImages.setNewProductImages(
                createNewProductImageList(productImages, productImageDTO.getAllImageIndexes()));

        return processedProductImages;
    }

    private static List<String> markImagesForDeleteAndGetKeys(List<ProductImage> productImages, List<String> deleteImageIndexes) {
        List<String> imageKeysForDelete = new ArrayList<>();
        if (deleteImageIndexes != null && !deleteImageIndexes.isEmpty()) {
            for (int i = 0; i < productImages.size(); i++) {
                var index = productImages.get(i).getIndex();
                boolean isDeleted = deleteImageIndexes.stream()
                        .map(Integer::parseInt)
                        .anyMatch(deletedIndex -> deletedIndex.equals(index));

                if (isDeleted) {
                    imageKeysForDelete.add(productImages.get(i).getImageKey());
                    productImages.set(i, null);
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

    private static List<ProductImage> createNewProductImageList(List<ProductImage> productImages, Map<String, String> allImageIndexes) {
        int finalProductImagesSize = (int) productImages.stream()
                .filter(Objects::nonNull)
                .count();

        List<ProductImage> newProductImages = new ArrayList<>(finalProductImagesSize);
        for (int i = 0; i < finalProductImagesSize; i++) {
            newProductImages.add(null);
        }

        for (var entry : allImageIndexes.entrySet()) {
            if (!Pattern.compile("image\\[\\d]").matcher(entry.getKey()).matches())
                continue;

            int currentIndex = Integer.parseInt(entry.getKey().replaceAll("\\D", ""));
            int newIndex = Integer.parseInt(entry.getValue());

            newProductImages.set(newIndex, productImages.get(currentIndex));
            newProductImages.get(newIndex).setIndex(newIndex);
        }

        return newProductImages;
    }
}
