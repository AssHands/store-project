package com.ak.store.catalogue.integration;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.Map;

@Service
public class S3Service {
    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
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
            throw new RuntimeException("error while put images");
        }
    }

    private void deleteOneImage(String imageKey) {
        var request = DeleteObjectRequest.builder()
                .bucket("products-photos")
                .key(imageKey)
                .build();

        s3Client.deleteObject(request);
    }

    public void deleteAllImage(List<String> imageKeysForDelete) {
        boolean a = false;
        for(var key : imageKeysForDelete) {
            copyImage("products-photos", "deleted-product-images", key);

            if(!isExist("deleted-product-images", key)) {
                throw new RuntimeException("error while delete images");
            }
        }

        for(var key : imageKeysForDelete) {
            deleteOneImage(key);
        }
    }

    public void compensateDeleteAllImage(List<String> imageKeysForCompensate) {
        for(var key : imageKeysForCompensate) {
            copyImage("deleted-product-images", "products-photos", key);

            if(!isExist("products-photos", key)) {
                throw new RuntimeException("error while compensate delete images");
            }
        }
    }

    public void putAllImage(Map<String, MultipartFile> imagesForAdd) {
        for(var image : imagesForAdd.entrySet()) {
            putOneImage(image.getValue(), image.getKey());
        }
    }

    public void compensatePutAllImage(Iterable<String> imageKeysForCompensate) {
        for(var key : imageKeysForCompensate) {
            deleteOneImage(key);

            if(isExist("products-photos", key)) {
                throw new RuntimeException("error while compensate put images");
            }
        }
    }

    private boolean isExist(String bucket, String key) {
        try{
            s3Client.headObject(b -> b.bucket(bucket).key(key));
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    private void copyImage(String sourceBucket, String destinationBucket, String key) {
        CopyObjectRequest copyReq = CopyObjectRequest.builder()
                .sourceBucket(sourceBucket)
                .sourceKey(key)
                .destinationBucket(destinationBucket)
                .destinationKey(key)
                .build();

        try {
            s3Client.copyObject(copyReq);
        } catch (NoSuchKeyException ignored) {}
    }
}
