package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.utils.ProductMapper;
import com.ak.store.common.dto.product.ProductDTO;
import com.ak.store.common.payload.product.ProductSearchResponse;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.catalogue.jdbc.ProductDao;
import com.ak.store.catalogue.test.ProductRepo;
import com.ak.store.common.payload.search.SearchAvailableFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductRepo productRepo;
    private final ElasticService esService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductRepo productRepo,
                              ElasticService esService, ProductMapper productMapper) {
        this.productDao = productDao;
        this.productRepo = productRepo;
        this.esService = esService;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO findOneById(Long id) {
        Product product = productDao.findOneById(id);

        if(product == null) {
            throw new RuntimeException("No Products found by id");
        }

        return productMapper.mapToDTO(product);
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
                        .map(productMapper::mapToDTO)
                        .toList());

        productSearchResponse.setSearchAfter(elasticSearchResult.getSearchAfter());

        return productSearchResponse;
    }

    @Override
    public AvailableFiltersResponse facet(SearchAvailableFilters searchAvailableFilters) {
        return esService.findAvailableFilters(searchAvailableFilters);
    }
}