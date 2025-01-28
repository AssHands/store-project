package com.ak.store.catalogue.service;

import com.ak.store.catalogue.Validator.ProductImageValidator;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.entity.relation.ProductCharacteristic;
import com.ak.store.catalogue.repository.*;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.catalogue.Validator.ProductCharacteristicValidator;
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
    private final ProductRepo productRepo;
    private final ElasticService esService;
    private final CatalogueMapper catalogueMapper;
    private final CharacteristicRepo characteristicRepo;
    private final CategoryRepo categoryRepo;
    private final ProductCharacteristicValidator productCharacteristicValidator;
    private final ProductImageValidator productImageValidator;
    private final S3Service s3Service;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    public ProductFullReadDTO findOneProductById(Long id) {
        return catalogueMapper.mapToProductFullReadDTO(
                productRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found")));
    }

    @Transactional
    public void saveOrUpdateAllImage(Long productId, Map<String, String> allImageIndexes,
                                     List<MultipartFile> addImages, List<String> deleteImageIndexes) {
        Product updatedProduct = productRepo.findOneWithImagesById(productId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        List<ProductImage> productImages = updatedProduct.getImages();
        productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages);

        List<String> imageKeysForDelete = markImagesForDelete(productImages, deleteImageIndexes);
        //LinkedHashMap for save order
        LinkedHashMap<String, MultipartFile> imagesForAdd = prepareImagesForAdd(updatedProduct, addImages);

        for (String key : imagesForAdd.keySet()) {
            productImages.add(ProductImage.builder()
                    .imageKey(key)
                    .product(Product.builder()
                            .id(productId)
                            .build())
                    .build());
        }

        List<ProductImage> newProductImages = createNewProductImagesList(productImages, allImageIndexes);

        //update images in DB
        updatedProduct.getImages().clear();
        updatedProduct.getImages().addAll(newProductImages);
        productRepo.saveAndFlush(updatedProduct);

        //update images in S3
        s3Service.deleteAllImage(imageKeysForDelete);
        s3Service.putAllImage(imagesForAdd);
    }

    private List<String> markImagesForDelete(List<ProductImage> productImages, List<String> deleteImageIndexes) {
        List<String> imageKeysForDelete = new ArrayList<>();
        if (deleteImageIndexes != null && !deleteImageIndexes.isEmpty()) {
            for (int i = 0; i < productImages.size(); i++) {
                var index = productImages.get(i).getIndex();
                boolean isDeleted = deleteImageIndexes.stream()
                        .map(Integer::parseInt)
                        .anyMatch(deletedIndex -> deletedIndex.equals(index));

                if (isDeleted) {
                    imageKeysForDelete.add(productImages.get(i).getImageKey());
                    productImages.set(i, null);
                }
            }
        }
        return imageKeysForDelete;
    }

    private LinkedHashMap<String, MultipartFile> prepareImagesForAdd(Product updatedProduct, List<MultipartFile> addImages) {
        //LinkedHashMap for save order
        LinkedHashMap<String, MultipartFile> imagesForAdd = new LinkedHashMap<>();
        if (addImages != null && !addImages.isEmpty()) {
            imagesForAdd = s3Service.generateImageKeys(updatedProduct, addImages);
        }
        return imagesForAdd;
    }

    private List<ProductImage> createNewProductImagesList(List<ProductImage> productImages, Map<String, String> allImageIndexes) {
        int finalProductImagesSize = (int) productImages.stream()
                .filter(Objects::nonNull)
                .count();

        List<ProductImage> newProductImages = new ArrayList<>(finalProductImagesSize);
        for (int i = 0; i < finalProductImagesSize; i++) {
            newProductImages.add(null);
        }

        for (var entry : allImageIndexes.entrySet()) {
            if (!Pattern.compile("image\\[\\d]").matcher(entry.getKey()).matches())
                continue;

            int currentIndex = Integer.parseInt(entry.getKey().replaceAll("\\D", ""));
            int newIndex = Integer.parseInt(entry.getValue());

            newProductImages.set(newIndex, productImages.get(currentIndex));
            newProductImages.get(newIndex).setIndex(newIndex);
        }

        return newProductImages;
    }

    public ProductSearchResponse findAllProductBySearch(SearchProductRequest searchProductRequest) {
        ElasticSearchResult elasticSearchResult = esService.findAllProduct(searchProductRequest);

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

    public SearchAvailableFiltersResponse findAvailableFilters(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        return esService.searchAvailableFilters(searchAvailableFiltersRequest);
    }

    public List<CategoryDTO> findAllCategory() {
        List<CategoryDTO> categories = categoryRepo.findAll().stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();

        return buildCategoryTree(categories);
    }

    public Filters findAllAvailableCharacteristicByCategory(Long categoryId) {
        return catalogueMapper.mapToFilters(characteristicRepo.findAllWithTextValuesByCategoryId(categoryId));
    }

    @Transactional
    //todo: when product doesn't exist, no errors will throw. MAKE IT
    public void deleteOneProduct(Long id) {
        productRepo.deleteById(id);
        esService.deleteOneProduct(id);
    }

    @Transactional
    public void createOneProduct(ProductWritePayload productPayload) {
        Product createdProduct = catalogueMapper.mapToProduct(productPayload.getProduct());
        createProductCharacteristics(createdProduct, productPayload.getCreateCharacteristics());

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAndFlush(createdProduct);
        esService.createOneProduct(catalogueMapper.mapToProductDocument(createdProduct));
    }

    @Transactional
    public void createAllProduct(List<ProductWritePayload> productPayloads) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productPayloads.size(); i++) {
            if(i > 0 && i % BATCH_SIZE == 0) {
                productRepo.saveAllAndFlush(products);
                esService.createAllProduct(products.stream()
                        .map(catalogueMapper::mapToProductDocument)
                        .toList());

                entityManager.clear();
                products.clear();
            }

            Product createdProduct = catalogueMapper.mapToProduct(productPayloads.get(i).getProduct());
            createProductCharacteristics(createdProduct, productPayloads.get(i).getCreateCharacteristics());
            products.add(createdProduct);
        }

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAllAndFlush(products);
        esService.createAllProduct(products.stream()
                .map(catalogueMapper::mapToProductDocument)
                .toList());
    }

    @Transactional
    public void updateOneProduct(ProductWritePayload productPayload, Long productId) {
        Product updatedProduct = productRepo.findOneWithCharacteristicsAndCategoryById(productId).orElseThrow(() -> new RuntimeException("Not found"));

        updateProduct(updatedProduct, productPayload.getProduct());

        if(!productPayload.getCreateCharacteristics().isEmpty()) {
            createProductCharacteristics(updatedProduct, productPayload.getCreateCharacteristics());
        }

        if(!productPayload.getUpdateCharacteristics().isEmpty()) {
            updateProductCharacteristics(updatedProduct, productPayload.getUpdateCharacteristics());
        }

        if(!productPayload.getDeleteCharacteristics().isEmpty()) {
            deleteProductCharacteristics(updatedProduct, productPayload.getDeleteCharacteristics());
        }

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAndFlush(updatedProduct);
        esService.updateOneProduct(catalogueMapper.mapToProductDocument(updatedProduct));
    }

    private void updateProduct(Product updatedProduct, ProductWriteDTO productWriteDTO) {
        if(productWriteDTO.getTitle() != null) {
            updatedProduct.setTitle(productWriteDTO.getTitle());
        }

        if(productWriteDTO.getDescription() != null) {
            updatedProduct.setDescription(productWriteDTO.getDescription());
        }

        boolean isUpdateFullPrice = false;
        if(productWriteDTO.getFullPrice() != null && productWriteDTO.getFullPrice() != updatedProduct.getFullPrice()) {
            updatedProduct.setFullPrice(productWriteDTO.getFullPrice());
            isUpdateFullPrice = true;
        }

        if(productWriteDTO.getDiscountPercentage() != null) {
            updatedProduct.setDiscountPercentage(productWriteDTO.getDiscountPercentage());
            int discount = updatedProduct.getFullPrice() * updatedProduct.getDiscountPercentage() / 100;
            int priceWithDiscount = updatedProduct.getFullPrice() - discount;
            updatedProduct.setCurrentPrice(priceWithDiscount);

        } else if(isUpdateFullPrice) {
            int discount = updatedProduct.getFullPrice() * updatedProduct.getDiscountPercentage() / 100;
            int priceWithDiscount = updatedProduct.getFullPrice() - discount;
            updatedProduct.setCurrentPrice(priceWithDiscount);
        }

        if(productWriteDTO.getCategoryId() != null
                && !updatedProduct.getCategory().getId().equals(productWriteDTO.getCategoryId())) {
            updatedProduct.setCategory(
                    Category.builder()
                            .id(productWriteDTO.getCategoryId())
                            .build()
            );
            updatedProduct.getCharacteristics().clear();
        }
    }

    private int findCharacteristicIndexById(List<ProductCharacteristic> characteristics, Long id) {
        for (int i = 0; i < characteristics.size(); i++) {
            if(characteristics.get(i).getCharacteristic().getId().equals(id))
                return i;
        }
        return -1;
    }

    private void createProductCharacteristics(Product updatedProduct, Set<ProductCharacteristicDTO> createCharacteristicsDTO) {
        Map<Long, List<String>> availableCharacteristics =
                characteristicRepo.findAllWithTextValuesByCategoryId(updatedProduct.getCategory().getId()).stream()
                        .collect(Collectors.toMap(
                                Characteristic::getId,
                                characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList()
                                )
                        );
        productCharacteristicValidator.validate(createCharacteristicsDTO, availableCharacteristics);

        List<Long> existingCharacteristicIds = updatedProduct.getCharacteristics().stream()
                .map(pc -> pc.getCharacteristic().getId())
                .toList();

        if(!existingCharacteristicIds.isEmpty()) {
            List<Long> creatingCharacteristicIds = createCharacteristicsDTO.stream()
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

        List<ProductCharacteristic> createdCharacteristics = createCharacteristicsDTO.stream()
                .map(c -> catalogueMapper.mapToProductCharacteristic(c, updatedProduct))
                .toList();

        updatedProduct.addCharacteristics(createdCharacteristics);
    }

    private void updateProductCharacteristics(Product updatedProduct, Set<ProductCharacteristicDTO> updateCharacteristicsDTO) {
        Map<Long, List<String>> availableCharacteristics =
                characteristicRepo.findAllWithTextValuesByCategoryId(updatedProduct.getCategory().getId()).stream()
                        .collect(Collectors.toMap(
                                        Characteristic::getId,
                                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList()
                                )
                        );
        productCharacteristicValidator.validate(updateCharacteristicsDTO, availableCharacteristics);

        for(var characteristic : updateCharacteristicsDTO) {
            int index = findCharacteristicIndexById(updatedProduct.getCharacteristics(), characteristic.getId());
            if(index != -1) {
                updatedProduct.getCharacteristics().get(index).setTextValue(characteristic.getTextValue());
                updatedProduct.getCharacteristics().get(index).setNumericValue(characteristic.getNumericValue());
            }
        }
    }

    private void deleteProductCharacteristics(Product updatedProduct, Set<ProductCharacteristicDTO> deleteCharacteristicsDTO) {
        for(var characteristic : deleteCharacteristicsDTO) {
            int index = findCharacteristicIndexById(updatedProduct.getCharacteristics(), characteristic.getId());
            if(index != -1)
                updatedProduct.getCharacteristics().remove(index);
        }
    }

    //todo: move to utils?
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