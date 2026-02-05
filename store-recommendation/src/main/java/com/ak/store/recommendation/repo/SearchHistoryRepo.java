package com.ak.store.recommendation.repo;

import java.util.List;
import java.util.UUID;

public interface SearchHistoryRepo {
    List<Long> findOne(UUID userId);

    void putOne(UUID userId, List<Long> categoryIds);
}
