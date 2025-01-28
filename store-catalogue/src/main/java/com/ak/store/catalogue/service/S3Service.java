package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Client s3Client;
    private final ProductRepo productRepo;

    public S3Service(S3Client s3Client, ProductRepo productRepo) {
        this.s3Client = s3Client;
        this.productRepo = productRepo;
    }

    public void putOneImage(MultipartFile image, String imageKey) {
        var request = PutObjectRequest.builder()
                .bucket("products-photos")
                .key(imageKey)
                .contentType(image.getContentType())
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));
        } catch (Exception e) {
            throw new RuntimeException("error when putting image into s3 storage");
        }
    }

    public void deleteOneImage(String imageKey) {
        var request = DeleteObjectRequest.builder()
                .bucket("products-photos")
                .key(imageKey)
                .build();

        s3Client.deleteObject(request);
    }

    public void deleteAllImage(List<String> imageKeysForDelete) {
        for(var deleteImageKey : imageKeysForDelete) {
            deleteOneImage(deleteImageKey);
        }
    }

    public void putAllImage(Map<String, MultipartFile> imagesForAdd) {
        for(var image : imagesForAdd.entrySet()) {
            putOneImage(image.getValue(), image.getKey());
        }
    }

    //LinkedHashMap for save order
    public LinkedHashMap<String, MultipartFile> generateImageKeys(Product product, List<MultipartFile> images) {
        LinkedHashMap<String, MultipartFile> imageKeys = new LinkedHashMap<>();
        for(var image : images) {
            imageKeys.put("category_%s/product_%s/%s".formatted(product.getCategory().getId(),
                    product.getId(), UUID.randomUUID() + "." + image.getContentType().split("/")[1]), image);
        }

        return imageKeys;
    }
}
