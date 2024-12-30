package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.relation.ProductCharacteristic;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.catalogue.utils.CatalogueValidator;
import com.ak.store.common.dto.catalogue.product.*;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.AvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.catalogue.jdbc.ProductDao;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductRepo productRepo;
    private final ElasticService esService;
    private final CatalogueMapper catalogueMapper;
    private final ProductCharacteristicRepo productCharacteristicRepo;
    private final CharacteristicRepo characteristicRepo;
    private final CategoryRepo categoryRepo;
    private final CatalogueValidator catalogueValidator;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductRepo productRepo, ElasticService esService,
                              CatalogueMapper catalogueMapper, ProductCharacteristicRepo productCharacteristicRepo,
                              CharacteristicRepo characteristicRepo, CategoryRepo categoryRepo,
                              CatalogueValidator catalogueValidator) {
        this.productDao = productDao;
        this.productRepo = productRepo;
        this.esService = esService;
        this.catalogueMapper = catalogueMapper;
        this.productCharacteristicRepo = productCharacteristicRepo;
        this.characteristicRepo = characteristicRepo;
        this.categoryRepo = categoryRepo;
        this.catalogueValidator = catalogueValidator;
    }

    @Override
    public ProductFullReadDTO findOneById(Long id) {
        return catalogueMapper.mapToProductFullReadDTO(
                productRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found")));
    }

    @Override
    public boolean deleteOneById(Long id) {
        productRepo.deleteById(id);
        return true;
    }

    @Override
    public ProductSearchResponse findAllBySearch(ProductSearchRequest productSearchRequest) {
        ElasticSearchResult elasticSearchResult = esService.findAll(productSearchRequest);

        if(elasticSearchResult == null) {
            throw new RuntimeException("No documents found");
        }

        ProductSearchResponse productSearchResponse = new ProductSearchResponse();

        productSearchResponse.setContent(
                 productRepo.findAllViewByIdIn(elasticSearchResult.getIds()).stream() //todo: make SORT
                        .map(catalogueMapper::mapToProductViewReadDTO)
                        .toList());

        productSearchResponse.setSearchAfter(elasticSearchResult.getSearchAfter());

        return productSearchResponse;
    }

    @Override
    public AvailableFiltersResponse facet(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        return esService.searchAvailableFilters(searchAvailableFiltersRequest);
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
    public Filters findAllAvailableCharacteristic(Long categoryId) {
        return catalogueMapper.mapToFilters(characteristicRepo.findTextValuesByCategoryId(categoryId));
    }

    @Override
    @Transactional
    public void createOneProduct(ProductWritePayload productPayload) {
        if(productPayload.getProduct().getCategoryId() == null) {
            throw new RuntimeException("category_id is null");
        }

        Product createdProduct = catalogueMapper.mapToProduct(productPayload.getProduct());

        List<ProductCharacteristic> productCharacteristics = new ArrayList<>();
        createProductCharacteristics(createdProduct, productCharacteristics,
                productPayload.getCreateCharacteristics(), true);

        productRepo.save(createdProduct);
        productCharacteristicRepo.saveAll(productCharacteristics);
    }

    @Override
    @Transactional
    public void createAllProduct(List<ProductWritePayload> productPayloads) {
        List<Product> products = new ArrayList<>();
        List<ProductCharacteristic> productCharacteristics = new ArrayList<>();
        for(var payload : productPayloads) {
            if(payload.getProduct().getCategoryId() == null) {
                throw new RuntimeException("category_id is null");
            }

            Product createdProduct = catalogueMapper.mapToProduct(payload.getProduct());
            products.add(createdProduct);

            createProductCharacteristics(createdProduct, productCharacteristics,
                    payload.getCreateCharacteristics(), true);
        }

        productRepo.saveAll(products);
        productCharacteristicRepo.saveAll(productCharacteristics);
    }

    @Override
    @Transactional
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

        List<ProductCharacteristic> productCharacteristics = updatedProduct.getCharacteristics();

        if(!productPayload.getCreateCharacteristics().isEmpty()) {
            createProductCharacteristics(updatedProduct, productCharacteristics,
                    productPayload.getCreateCharacteristics(), isNewCategory);
        }

        if(!productPayload.getUpdateCharacteristics().isEmpty() && !isNewCategory) {
            updateProductCharacteristics(updatedProduct, productCharacteristics,
                    productPayload.getUpdateCharacteristics());
        }

        if(!productPayload.getDeleteCharacteristics().isEmpty() && !isNewCategory) {
            deleteProductCharacteristics(productCharacteristics, productPayload.getDeleteCharacteristics());
        }

        productRepo.save(updatedProduct);
        productCharacteristicRepo.saveAll(productCharacteristics);
        return true;
    }

    private int findCharacteristicIndexById(List<ProductCharacteristic> characteristics, Long id) {
        for (int i = 0; i < characteristics.size(); i++) {
            if(characteristics.get(i).getCharacteristic().getId().equals(id))
                return i;
        }
        return -1;
    }

    private void createProductCharacteristics(Product updatedProduct, List<ProductCharacteristic> productCharacteristics,
                                              Set<ProductCharacteristicDTO> updatedCharacteristics, boolean isNewCategory) {
        catalogueValidator.validateCharacteristics(updatedCharacteristics, updatedProduct.getCategory().getId());

        if(!isNewCategory) {
            List<Long> existingCharacteristicIds = updatedProduct.getCharacteristics().stream()
                    .map(pc -> pc.getCharacteristic().getId())
                    .toList();

            List<Long> creatingCharacteristicIds = updatedCharacteristics.stream()
                            .map(ProductCharacteristicDTO::getId)
                            .toList();

            Optional<Long> notUniqCharacteristicId = creatingCharacteristicIds.stream()
                    .filter(existingCharacteristicIds::contains)
                    .findFirst();

            if(notUniqCharacteristicId.isPresent()) {
                throw new RuntimeException("Characteristic with id=%s already exists"
                        .formatted(notUniqCharacteristicId.get()));
            }
        }

        productCharacteristics.addAll(updatedCharacteristics.stream()
                .map(characteristic -> catalogueMapper.mapToProductCharacteristic(characteristic, updatedProduct))
                .toList());
    }

    private void updateProductCharacteristics(Product updatedProduct, List<ProductCharacteristic> productCharacteristics,
                                              Set<ProductCharacteristicDTO> updatedCharacteristics) {
        catalogueValidator.validateCharacteristics(updatedCharacteristics, updatedProduct.getCategory().getId());

        for(var characteristic : updatedCharacteristics) {
            int index = findCharacteristicIndexById(productCharacteristics, characteristic.getId());
            if(index != -1) {
                productCharacteristics.get(index).setTextValue(characteristic.getTextValue());
                productCharacteristics.get(index).setNumericValue(characteristic.getNumericValue());
            }
        }
    }

    private void deleteProductCharacteristics(List<ProductCharacteristic> productCharacteristics,
                                              Set<ProductCharacteristicDTO> updatedCharacteristics) {
        for(var characteristic : updatedCharacteristics) {
            int index = findCharacteristicIndexById(productCharacteristics, characteristic.getId());
            if(index != -1) {
                productCharacteristics.remove(index);
            }
        }
    }

    private List<CategoryDTO> buildCategoryTree(List<CategoryDTO> categories) { //todo: move to utils?
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