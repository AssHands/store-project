package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCharacteristicRepo extends JpaRepository<ProductCharacteristic, Long> {
    List<ProductCharacteristic> findAllByProductId(Long productId);

    //todo check sql query
    void deleteAllByProductIdAndCharacteristicIdIn(Long productId, List<Long> characteristicIds);

    //todo check sql query
    void deleteAllByProductId(Long productId);
}
