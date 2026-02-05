package com.ak.store.recommendation.repo;

import com.ak.store.recommendation.model.document.Product;

import java.util.List;

public interface ProductRepo {
    List<Product> findRandomByCategoryIds(List<Long> categoryIds, int size);

    List<Product> findRandom(int size);
}
