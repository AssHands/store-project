package com.ak.store.catalogue.service;

import com.ak.store.catalogue.utils.CatalogueUtils;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.common.dto.catalogue.product.*;
import com.ak.store.common.dto.search.Filters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


//    Session session = entityManager.unwrap(Session.class);
//    SessionFactory sessionFactory = session.getSessionFactory();
//    sessionFactory.getCache();
//    Session session = entityManager.unwrap(Session.class);
//    Statistics statistics = session.getSessionFactory().getStatistics();
//    CacheRegionStatistics cacheStatistics = statistics.getDomainDataRegionStatistics("static-data");
@Service
@RequiredArgsConstructor
public class CatalogueService {
    private final CatalogueMapper catalogueMapper;
    private final CharacteristicRepo characteristicRepo;
    private final CategoryRepo categoryRepo;

    private final CatalogueUtils catalogueUtils;

    public List<CategoryDTO> findAllCategory() {
        List<CategoryDTO> categories = categoryRepo.findAll().stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();

        return catalogueUtils.buildCategoryTree(categories);
    }

    //todo: returns null when none. FIX
    public Filters findAllAvailableCharacteristicByCategory(Long categoryId) {
        return catalogueMapper.mapToFilters(characteristicRepo.findAllWithTextValuesByCategoryId(categoryId));
    }
}