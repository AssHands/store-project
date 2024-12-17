package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.relation.ProductCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCharacteristicRepo extends JpaRepository<ProductCharacteristic, Long> {
}
