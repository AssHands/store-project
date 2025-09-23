package com.ak.store.catalogue.repository;

import com.ak.store.catalogue.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
    List<Image> findAllByProductId(Long productId);

    void deleteAllByProductId(Long productId);
}
