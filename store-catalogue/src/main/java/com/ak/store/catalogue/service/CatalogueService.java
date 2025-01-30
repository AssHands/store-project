package com.ak.store.catalogue.service;

import com.ak.store.catalogue.validator.ProductImageValidator;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.entity.ProductCharacteristic;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.catalogue.validator.ProductCharacteristicValidator;
import com.ak.store.common.dto.catalogue.product.*;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.SearchProductRequest;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


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

    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryDTO> findAllCategory() {
        List<CategoryDTO> categories = categoryRepo.findAll().stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();

        return buildCategoryTree(categories);
    }

    //todo: returns null when none. FIX
    public Filters findAllAvailableCharacteristicByCategory(Long categoryId) {
        return catalogueMapper.mapToFilters(characteristicRepo.findAllWithTextValuesByCategoryId(categoryId));
    }

    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) {
        Map<Long, CategoryDTO> categoryMap = new LinkedHashMap<>();
        List<CategoryDTO> rootCategories = new ArrayList<>();

        for (CategoryDTO category : categories) {
            categoryMap.put(category.getId(), category);
        }

        for (CategoryDTO category : categories) {
            if (category.getParentId() == null) {
                rootCategories.add(category);
            } else {
                CategoryDTO parent = categoryMap.get(category.getParentId());
                if (parent != null) {
                    parent.getChildCategories().add(category);
                } else {
                    throw new RuntimeException("no parent with id " + category.getParentId());
                }
            }
        }

        return rootCategories;
    }
}