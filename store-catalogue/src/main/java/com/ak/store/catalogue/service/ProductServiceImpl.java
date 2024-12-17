package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.ne.Category;
import com.ak.store.catalogue.model.entity.ne.Characteristic;
import com.ak.store.catalogue.model.entity.ne.Product;
import com.ak.store.catalogue.model.entity.ne.relation.ProductCharacteristic;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.catalogue.utils.CatalogueValidator;
import com.ak.store.common.dto.catalogue.product.AvailableCharacteristicValuesDTO;
import com.ak.store.common.dto.catalogue.product.CategoryDTO;
import com.ak.store.common.dto.catalogue.product.ProductFullReadDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.catalogue.jdbc.ProductDao;
import com.ak.store.common.payload.search.SearchAvailableFilters;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductRepo productRepo;
    private final TextValueRepo textValueRepo;
    private final ElasticService esService;
    private final CatalogueMapper catalogueMapper;
    private final ProductCharacteristicRepo productCharacteristicRepo;
    private final CharacteristicRepo characteristicRepo;
    private final CategoryRepo categoryRepo;
    private final CatalogueValidator catalogueValidator;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductRepo productRepo,
                              TextValueRepo textValueRepo, ElasticService esService, CatalogueMapper catalogueMapper,
                              ProductCharacteristicRepo productCharacteristicRepo, CharacteristicRepo characteristicRepo,
                              CategoryRepo categoryRepo, CatalogueValidator catalogueValidator) {
        this.productDao = productDao;
        this.productRepo = productRepo;
        this.textValueRepo = textValueRepo;
        this.esService = esService;
        this.catalogueMapper = catalogueMapper;
        this.productCharacteristicRepo = productCharacteristicRepo;
        this.characteristicRepo = characteristicRepo;
        this.categoryRepo = categoryRepo;
        this.catalogueValidator = catalogueValidator;
    }

    @Override
    public ProductFullReadDTO findOneById(Long id) {
        return null;
    }

    @Override
    public boolean deleteOneById(Long id) {
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
                        .map(catalogueMapper::mapToProductReadDTO)
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
        List<CategoryDTO> categories = categoryRepo.findAll().stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();

        return buildCategoryTree(categories);
    }

    @Override
    public List<CategoryDTO> findAllCategory(String name) {
        return categoryRepo.findAllByNameContainingIgnoreCase(name).stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();
    }

    @Override
    public List<AvailableCharacteristicValuesDTO> findAllAvailableCharacteristic(Long categoryId) {
        return characteristicRepo.findTextValuesByCategoryId(categoryId).stream()
                .map(catalogueMapper::mapToAvailableCharacteristicValuesDTO)
                .toList();
    }

    @Override
    @Transactional
    public boolean createOneProduct(ProductWritePayload productPayload) {
        Product createdProduct = catalogueMapper.mapToProduct(productPayload);

        List<Characteristic> availableCharacteristics =
                characteristicRepo.findTextValuesByCategoryId(createdProduct.getCategory().getId());
        createProductCharacteristics(createdProduct, productPayload, availableCharacteristics);

        productRepo.save(createdProduct);
        return false;
    }

    @Override
    @Transactional
    public boolean createAllProduct(List<ProductWritePayload> productPayloads) {
        List<Product> products = new ArrayList<>();
        for(var payload : productPayloads) {
            Product createdProduct = catalogueMapper.mapToProduct(payload);

            List<Characteristic> availableCharacteristics =
                    characteristicRepo.findTextValuesByCategoryId(createdProduct.getCategory().getId());
            createProductCharacteristics(createdProduct, payload, availableCharacteristics);

            products.add(createdProduct);
        }

        int fromIndex = 0;
        for (int i = 0; i < productPayloads.size(); i++) {
            if(i % (BATCH_SIZE - 1) == 0 && i != 0) {
                productRepo.saveAllAndFlush(products.subList(fromIndex, i));
                fromIndex = i;
            }
        }

        if(fromIndex + 1 != productPayloads.size() || productPayloads.size() == 1)
            productRepo.saveAllAndFlush(products.subList(fromIndex, products.size()));

        return true;
    }

    @Transactional
    @Override
    public boolean updateOneProduct(ProductWritePayload productPayload, Long productId) {
        Product updatedProduct = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Not found"));
        ProductWriteDTO productDTO = productPayload.getProduct();


        if(productDTO.getTitle() != null) {
            updatedProduct.setTitle(productDTO.getTitle());
        }

        if(productDTO.getDescription() != null) {
            updatedProduct.setDescription(productDTO.getDescription());
        }

        if(productDTO.getPrice() != null) {
            updatedProduct.setPrice(productDTO.getPrice());
        }

        boolean isNewCategory = false;
        if(productDTO.getCategoryId() != null
                && !updatedProduct.getCategory().getId().equals(productDTO.getCategoryId())) {
            updatedProduct.setCategory(
                    Category.builder()
                            .id(productDTO.getCategoryId())
                            .build()
            );
            isNewCategory = true;
            updatedProduct.getCharacteristics().clear();
        }

        List<Characteristic> availableCharacteristics = new ArrayList<>();

        if(!productPayload.getCreateCharacteristics().isEmpty()
                || (!productPayload.getUpdateCharacteristics().isEmpty() && !isNewCategory)) {
            availableCharacteristics = characteristicRepo.findTextValuesByCategoryId(updatedProduct.getCategory().getId());
        }

        if(!productPayload.getCreateCharacteristics().isEmpty()) {
            createProductCharacteristics(updatedProduct, productPayload, availableCharacteristics);
        }

        if(!productPayload.getUpdateCharacteristics().isEmpty() && !isNewCategory) {
            updateProductCharacteristics(updatedProduct, productPayload, availableCharacteristics);
        }

        if(!productPayload.getDeleteCharacteristics().isEmpty() && !isNewCategory) {
            deleteProductCharacteristics(updatedProduct, productPayload);
        }

        productRepo.save(updatedProduct);
        return true;
    }

    private void createProductCharacteristics(Product product, ProductWritePayload productPayload,
                                              List<Characteristic> availableCharacteristics) {
        List<ProductCharacteristic> createdCharacteristics = new ArrayList<>();
        for(var characteristic : productPayload.getCreateCharacteristics()) {
            int index = findCharacteristicIndex(availableCharacteristics, characteristic.getCharacteristicId());
            int productCharacteristicIndex = findProductCharacteristicIndex(
                    product.getCharacteristics(), characteristic.getCharacteristicId());

            if(productCharacteristicIndex != -1) {
                throw new RuntimeException("Characteristic with id=%s already exists"
                        .formatted(characteristic.getCharacteristicId()));
            }

            if(index == -1) {
                throw new RuntimeException("Characteristic with id=%s is not available"
                        .formatted(characteristic.getCharacteristicId()));
            }

            catalogueValidator.validateCharacteristic(availableCharacteristics.get(index), characteristic);

            createdCharacteristics.add(
                    ProductCharacteristic.builder()
                            .product(product)
                            .characteristic(
                                    Characteristic.builder()
                                            .id(characteristic.getCharacteristicId())
                                            .build())
                            .numericValue(characteristic.getNumericValue())
                            .textValue(characteristic.getTextValue())
                            .build());
        }

        product.addCharacteristics(createdCharacteristics);
    }

    private void updateProductCharacteristics(Product product, ProductWritePayload productPayload,
                                              List<Characteristic> availableCharacteristics) {
        for(var characteristic : productPayload.getUpdateCharacteristics()) {
            int updatedIndex = findProductCharacteristicIndex(product.getCharacteristics(), characteristic.getCharacteristicId());
            int availableIndex = findCharacteristicIndex(availableCharacteristics, characteristic.getCharacteristicId());

            catalogueValidator.validateCharacteristic(availableCharacteristics.get(availableIndex), characteristic);
            if(updatedIndex == -1) {
                throw new RuntimeException("Characteristic for update with id=%s doesn't exist"
                        .formatted(characteristic.getCharacteristicId()));
            }

            product.getCharacteristics().get(updatedIndex).setTextValue(characteristic.getTextValue());
            product.getCharacteristics().get(updatedIndex).setNumericValue(characteristic.getNumericValue());

        }
    }

    private void deleteProductCharacteristics(Product product, ProductWritePayload productPayload) {
        for(var characteristic : productPayload.getDeleteCharacteristics()) {
            int index = findProductCharacteristicIndex(product.getCharacteristics(), characteristic.getCharacteristicId());
            if (index == -1) {
                throw new RuntimeException("Characteristic for delete with id=%s doesn't exist"
                        .formatted(characteristic.getCharacteristicId()));
            }

            product.getCharacteristics().remove(index);
        }
    }

    private int findProductCharacteristicIndex(List<ProductCharacteristic> characteristics, Long id) {
        for (int i = 0; i < characteristics.size(); i++) {
            if(characteristics.get(i).getCharacteristic().getId().equals(id))
                return i;
        }
        return -1;
    }

    private int findCharacteristicIndex(List<Characteristic> availableList,Long characteristicId) {
        for(int i = 0; i < availableList.size(); i++) {
            if(availableList.get(i).getId().equals(characteristicId))
                return i;
        }
        return -1;
    }

    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) { //todo: move to utils may be?
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