package com.ak.store.recommendation.repo;

import java.util.List;

public interface CatalogueRedisRepo {
    List<Long> findAllRelatedCategoryByCategoryIds(List<Long> categoryIds);
}
