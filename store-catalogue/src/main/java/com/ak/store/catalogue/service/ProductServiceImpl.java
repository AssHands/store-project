package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.common.dto.catalogue.AvailableCharacteristicDTO;
import com.ak.store.common.dto.catalogue.CategoryDTO;
import com.ak.store.common.dto.catalogue.ProductReadDTO;
import com.ak.store.common.payload.product.ProductSearchResponse;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.catalogue.jdbc.ProductDao;
import com.ak.store.catalogue.test.ProductRepo;
import com.ak.store.common.payload.search.SearchAvailableFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductRepo productRepo;
    private final ElasticService esService;
    private final CatalogueMapper catalogueMapper;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductRepo productRepo,
                              ElasticService esService, CatalogueMapper catalogueMapper) {
        this.productDao = productDao;
        this.productRepo = productRepo;
        this.esService = esService;
        this.catalogueMapper = catalogueMapper;
    }

    @Override
    public ProductReadDTO findOneById(Long id) {
        Product product = productDao.findOneById(id);

        if(product == null) {
            throw new RuntimeException("No Products found by id");
        }

        return catalogueMapper.mapToProductSearchDTO(product);
    }

    @Override
    public boolean deleteOneById(Long id) {
        if(productDao.deleteOneById(id)) {
            return true;
        }
        return false;
    }

    @Override
    public ProductSearchResponse findAllBySearch(ProductSearchRequest productSearchRequest) {
        ElasticSearchResult elasticSearchResult = esService.findAll(productSearchRequest);

        if(elasticSearchResult == null) {
            throw new RuntimeException("No documents found");
        }

        ProductSearchResponse productSearchResponse = new ProductSearchResponse();

        productSearchResponse.setContent(
                productDao.findAllByIds(elasticSearchResult.getIds(), productSearchRequest.getSort())
                        .stream()
                        .map(catalogueMapper::mapToProductSearchDTO)
                        .toList());

        productSearchResponse.setSearchAfter(elasticSearchResult.getSearchAfter());

        return productSearchResponse;
    }

    @Override
    public AvailableFiltersResponse facet(SearchAvailableFilters searchAvailableFilters) {
        return esService.searchAvailableFilters(searchAvailableFilters);
    }

    @Override
    public List<CategoryDTO> findAllCategory() {
        List<CategoryDTO> categories = productDao.findAllCategory().stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();

        return buildCategoryTree(categories);
    }

    @Override
    public List<CategoryDTO> findAllCategoryByName(String name) {
        return productDao.findAllCategoryByName(name).stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();
    }

    @Override
    public List<AvailableCharacteristicDTO> findAllAvailableCharacteristic(Long categoryId) {
        return catalogueMapper.mapToAvailableCharacteristicDTO(
                productDao.findAllAvailableCharacteristic(categoryId));
    }

    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) {
        Map<Long, CategoryDTO> categoryMap = new HashMap<>();
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
                }
            }
        }

        return rootCategories;
    }
}