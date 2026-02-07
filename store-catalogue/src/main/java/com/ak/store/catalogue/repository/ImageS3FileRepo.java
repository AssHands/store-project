package com.ak.store.catalogue.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ImageS3FileRepo implements ImageFileRepo {
    private final S3Client s3Client;

    private final String BUCKET_NAME = "products-images";
    private final String DELETED_BUCKET_NAME = "deleted-products-images";

    @Override
    public void addOneImage(MultipartFile image, String imageKey) {
        var request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
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
                .bucket(BUCKET_NAME)
                .key(imageKey)
                .build();

        s3Client.deleteObject(request);
    }

    @Override
    public void deleteAllImage(List<String> imageKeysForDelete) {
        for (var key : imageKeysForDelete) {
            copyOneImage(BUCKET_NAME, DELETED_BUCKET_NAME, key);

            if (!isExist(DELETED_BUCKET_NAME, key)) {
                throw new RuntimeException("error while delete images");
            }
        }

        for (var key : imageKeysForDelete) {
            deleteOneImage(key);
        }
    }

    @Override
    public void compensateDeleteAllImage(List<String> imageKeysForCompensate) {
        for (var key : imageKeysForCompensate) {
            copyOneImage(DELETED_BUCKET_NAME, BUCKET_NAME, key);

            if (!isExist(BUCKET_NAME, key)) {
                throw new RuntimeException("error while compensate delete images");
            }
        }
    }

    @Override
    public void addAllImage(Map<String, MultipartFile> imagesForAdd) {
        for (var image : imagesForAdd.entrySet()) {
            addOneImage(image.getValue(), image.getKey());
        }
    }

    @Override
    public void compensateAddAllImage(Iterable<String> imageKeysForCompensate) {
        for (var key : imageKeysForCompensate) {
            deleteOneImage(key);

            if (isExist(BUCKET_NAME, key)) {
                throw new RuntimeException("error while compensate put images");
            }
        }
    }

    private boolean isExist(String bucket, String key) {
        try {
            s3Client.headObject(b -> b.bucket(bucket).key(key));
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    private void copyOneImage(String sourceBucket, String destinationBucket, String key) {
        CopyObjectRequest copyReq = CopyObjectRequest.builder()
                .sourceBucket(sourceBucket)
                .sourceKey(key)
                .destinationBucket(destinationBucket)
                .destinationKey(key)
                .build();

        try {
            s3Client.copyObject(copyReq);
        } catch (NoSuchKeyException ignored) {
        }
    }
}
