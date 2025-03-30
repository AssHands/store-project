package com.ak.store.recommendation.repo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public class CatalogueRedisRepoImpl implements CatalogueRedisRepo {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String RELATED_CATEGORY_KEY = "related_category:";

    public CatalogueRedisRepoImpl(@Qualifier("stringRedisTemplateForCatalogue") StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<Long> findAllRelatedCategoryByCategoryIds(List<Long> categoryIds) {
        Set<String> relatedCategories = new HashSet<>();
        for (Long id : categoryIds) {
            Set<String> related = stringRedisTemplate.opsForSet().members(RELATED_CATEGORY_KEY + id);
            if (related != null) {
                relatedCategories.addAll(related);
            }
        }

        return Stream.concat(
                        relatedCategories.stream()
                                .map(Long::parseLong),
                        categoryIds.stream())
                .distinct()
                .toList();
    }
}
