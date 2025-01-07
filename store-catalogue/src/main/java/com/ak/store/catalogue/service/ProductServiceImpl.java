package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.document.ProductDocument;
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
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.ProductSearchRequest;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ElasticService esService;
    private final CatalogueMapper catalogueMapper;
    private final ProductCharacteristicRepo productCharacteristicRepo;
    private final CharacteristicRepo characteristicRepo;
    private final CategoryRepo categoryRepo;
    private final CatalogueValidator catalogueValidator;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, ElasticService esService,CatalogueMapper catalogueMapper,
                              ProductCharacteristicRepo productCharacteristicRepo,CharacteristicRepo characteristicRepo,
                              CategoryRepo categoryRepo, CatalogueValidator catalogueValidator) {
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
    public SearchAvailableFiltersResponse facet(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
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
    public void deleteOneProduct(Long id) {
        productRepo.deleteById(id);

        try {
            esService.deleteOneProduct(id);
        } catch (IOException e) {
            throw new RuntimeException("delete document error");
        }
    }

    @Override
    @Transactional
    public void createOneProduct(ProductWritePayload productPayload) { //todo: when jakarta validation fail, document will index in ES anyway. FIX
        if(productPayload.getProduct().getCategoryId() == null) {
            throw new RuntimeException("category_id is null");
        }

        Product createdProduct = catalogueMapper.mapToProduct(productPayload.getProduct());

        List<ProductCharacteristic> productCharacteristics = new ArrayList<>();
        createProductCharacteristics(createdProduct, productCharacteristics,
                productPayload.getCreateCharacteristics());

        productRepo.save(createdProduct);
        productCharacteristicRepo.saveAll(productCharacteristics);

        try {
            esService.createOneProduct(catalogueMapper.mapToProductDocument(createdProduct));
        } catch (IOException e) {
            throw new RuntimeException("index document error");
        }
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
                    payload.getCreateCharacteristics());
        }

        productRepo.saveAll(products);
        productCharacteristicRepo.saveAll(productCharacteristics);

        try {
            esService.createAllProduct(products.stream().map(catalogueMapper::mapToProductDocument).toList());
        } catch (IOException e) {
            throw new RuntimeException("bulk document error");
        }
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

        boolean isUpdateFullPrice = false;
        if(productDTO.getFullPrice() != null && productDTO.getFullPrice() != updatedProduct.getFullPrice()) {
            updatedProduct.setFullPrice(productDTO.getFullPrice());
            isUpdateFullPrice = true;
        }

        if(productDTO.getDiscountPercentage() != null) {
            updatedProduct.setDiscountPercentage(productDTO.getDiscountPercentage());
            int discount = updatedProduct.getFullPrice() * updatedProduct.getDiscountPercentage() / 100;
            int priceWithDiscount = updatedProduct.getFullPrice() - discount;
            updatedProduct.setCurrentPrice(priceWithDiscount);

        } else if(isUpdateFullPrice) {
            int discount = updatedProduct.getFullPrice() * updatedProduct.getDiscountPercentage() / 100;
            int priceWithDiscount = updatedProduct.getFullPrice() - discount;
            updatedProduct.setCurrentPrice(priceWithDiscount);
        }

        if(productDTO.getCategoryId() != null
                && !updatedProduct.getCategory().getId().equals(productDTO.getCategoryId())) {
            updatedProduct.setCategory(
                    Category.builder()
                            .id(productDTO.getCategoryId())
                            .build()
            );
            updatedProduct.getCharacteristics().clear();
        }

        List<ProductCharacteristic> createProductCharacteristics = new ArrayList<>();
        List<ProductCharacteristic> deleteProductCharacteristics = new ArrayList<>();

        if(!productPayload.getCreateCharacteristics().isEmpty()) {
            createProductCharacteristics(updatedProduct, createProductCharacteristics,
                    productPayload.getCreateCharacteristics());
        }

        if(!productPayload.getUpdateCharacteristics().isEmpty()) {
            updateProductCharacteristics(updatedProduct, createProductCharacteristics,
                    productPayload.getUpdateCharacteristics());
        }

        if(!productPayload.getDeleteCharacteristics().isEmpty()) {
            deleteProductCharacteristics(updatedProduct, deleteProductCharacteristics,
                    productPayload.getDeleteCharacteristics());
        }

        productCharacteristicRepo.saveAll(createProductCharacteristics);
        productCharacteristicRepo.deleteAll(deleteProductCharacteristics); //todo: need a check for empty collection?
        productRepo.save(updatedProduct);

        try {
            esService.updateOneProduct(catalogueMapper.mapToProductDocument(updatedProduct));
        } catch (IOException e) {
            throw new RuntimeException("update document error");
        }

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
                                              Set<ProductCharacteristicDTO> createdCharacteristics) {
        catalogueValidator.validateCharacteristics(createdCharacteristics, updatedProduct.getCategory().getId());

        List<Long> existingCharacteristicIds = updatedProduct.getCharacteristics().stream()
                .map(pc -> pc.getCharacteristic().getId())
                .toList();

        if(!existingCharacteristicIds.isEmpty()) {
            List<Long> creatingCharacteristicIds = createdCharacteristics.stream()
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

        List<ProductCharacteristic> list = createdCharacteristics.stream()
                .map(characteristic -> catalogueMapper.mapToProductCharacteristic(characteristic, updatedProduct))
                .toList();

        updatedProduct.addCharacteristics(list);
        productCharacteristics.addAll(list);
    }

    private void updateProductCharacteristics(Product updatedProduct, List<ProductCharacteristic> productCharacteristics,
                                              Set<ProductCharacteristicDTO> updatedCharacteristics) {
        catalogueValidator.validateCharacteristics(updatedCharacteristics, updatedProduct.getCategory().getId());

        for(var characteristic : updatedCharacteristics) {
            int index = findCharacteristicIndexById(updatedProduct.getCharacteristics(), characteristic.getId());
            if(index != -1) {
                updatedProduct.getCharacteristics().get(index).setTextValue(characteristic.getTextValue());
                updatedProduct.getCharacteristics().get(index).setNumericValue(characteristic.getNumericValue());
                productCharacteristics.add(updatedProduct.getCharacteristics().get(index));
            }
        }
    }

    private void deleteProductCharacteristics(Product updatedProduct, List<ProductCharacteristic> productCharacteristics,
                                              Set<ProductCharacteristicDTO> deletedCharacteristics) {
        for(var characteristic : deletedCharacteristics) {
            int index = findCharacteristicIndexById(updatedProduct.getCharacteristics(), characteristic.getId());
            if(index != -1) {
                productCharacteristics.add(updatedProduct.getCharacteristics().get(index));
                updatedProduct.getCharacteristics().remove(index);
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