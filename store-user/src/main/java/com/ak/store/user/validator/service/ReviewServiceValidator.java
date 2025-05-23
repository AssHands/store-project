//package com.ak.store.user.validator.service;
//
//import com.ak.store.user.feign.CatalogueFeign;
//import com.ak.store.user.repository.ReviewRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Component
//public class ReviewServiceValidator {
//    private final CatalogueFeign catalogueFeign;
//
//    private final ReviewRepo reviewRepo;
//
//    public void validateCreation(Long productId, String consumerId) {
//        if(!catalogueFeign.existOne(productId)) {
//            throw new RuntimeException("product with id=%d is not exists".formatted(productId));
//        }
//
//        boolean isExist = reviewRepo.findOneByProductIdAndConsumerId(productId, UUID.fromString(consumerId)).isPresent();
//        if(isExist) {
//            throw new RuntimeException("this consumer already has review on this product");
//        }
//    }
//}
