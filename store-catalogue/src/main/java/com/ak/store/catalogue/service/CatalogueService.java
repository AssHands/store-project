package com.ak.store.catalogue.service;

import com.ak.store.catalogue.util.CatalogueUtils;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.common.dto.catalogue.product.*;
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

    public List<CategoryDTO> findAllCategory() {
        List<CategoryDTO> categories = categoryRepo.findAll().stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();

        return CatalogueUtils.buildCategoryTree(categories);
    }

    public List<CharacteristicDTO> findAllAvailableCharacteristicByCategory(Long categoryId) {
        return characteristicRepo.findAllWithTextValuesByCategoryId(categoryId).stream()
                .map(catalogueMapper::mapToCharacteristicDTO)
                .toList();
    }
}