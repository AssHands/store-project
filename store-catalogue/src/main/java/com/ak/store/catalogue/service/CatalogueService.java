package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductImage;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.CacheRegionStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;


@Service
public class CatalogueService {
    private final ProductRepo productRepo;
    private final ElasticService esService;
    private final CatalogueMapper catalogueMapper;
    private final ProductCharacteristicRepo productCharacteristicRepo;
    private final CharacteristicRepo characteristicRepo;
    private final CategoryRepo categoryRepo;
    private final CatalogueValidator catalogueValidator;
    private final S3Service s3Service;
    private final ProductImageRepo productImageRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CatalogueService(ProductRepo productRepo, ElasticService esService, CatalogueMapper catalogueMapper,
                            ProductCharacteristicRepo productCharacteristicRepo, CharacteristicRepo characteristicRepo,
                            CategoryRepo categoryRepo, CatalogueValidator catalogueValidator, S3Service s3Service,
                            ProductImageRepo productImageRepo) {
        this.productRepo = productRepo;
        this.esService = esService;
        this.catalogueMapper = catalogueMapper;
        this.productCharacteristicRepo = productCharacteristicRepo;
        this.characteristicRepo = characteristicRepo;
        this.categoryRepo = categoryRepo;
        this.catalogueValidator = catalogueValidator;
        this.s3Service = s3Service;
        this.productImageRepo = productImageRepo;
    }

    public ProductFullReadDTO findOneProductById(Long id) {
        return catalogueMapper.mapToProductFullReadDTO(
                productRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found")));
    }

    @Transactional
    public void saveOrUpdateAllImage(Long productId, Map<String, String> allImagePositions,
                                     List<MultipartFile> addImages, List<String> deleteImageIndexes) {
        List<ProductImage> productImages = productImageRepo.findAllByProductId(productId);
        catalogueValidator.validateImages(allImagePositions, addImages, deleteImageIndexes, productImages);

        List<Long> imageIdsForDelete = new ArrayList<>();
        List<String> imageKeysForDelete = new ArrayList<>();

        //set null for deleted images
        //set null for save indexes of list
        if(deleteImageIndexes != null && !deleteImageIndexes.isEmpty()) {
            for (int i = 0; i < productImages.size(); i++) {
                var index = productImages.get(i).getIndex();
                boolean isDeleted = deleteImageIndexes.stream()
                        .map(Integer::parseInt)
                        .anyMatch(deletedIndex -> deletedIndex.equals(index));

                if(isDeleted) {
                    imageKeysForDelete.add(productImages.get(i).getImageKey());
                    imageIdsForDelete.add(productImages.get(i).getId());
                    productImages.set(i, null);
                }
            }
        }

        //generate keys for new images and wrap to Map<Key, Image>
        Map<String, MultipartFile> imagesForAdd = new LinkedHashMap<>();
        if(addImages != null && !addImages.isEmpty()) {
            imagesForAdd = s3Service.generateImageKeys(productId, addImages);
        }

        //add new images to list
        for(String key : imagesForAdd.keySet()) {
            productImages.add(ProductImage.builder()
                    .imageKey(key)
                    .product(Product.builder()
                            .id(productId)
                            .build())
                    .build());
        }

        int size = (int) productImages.stream()
                .filter(Objects::nonNull)
                .count();

        //make new entity list to save in DB
        //set null for easy insertion by index
        List<ProductImage> newProductImages = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            newProductImages.add(null);
        }

        //deleting and moving images
        for(var entry : allImagePositions.entrySet()) {
            if(!Pattern.compile("image\\[\\d]").matcher(entry.getKey()).matches())
                continue;

            int currentPosition = Integer.parseInt(entry.getKey().replaceAll("\\D", ""));
            int newPosition = Integer.parseInt(entry.getValue());

            newProductImages.set(newPosition, productImages.get(currentPosition));
            newProductImages.get(newPosition).setIndex(newPosition);
        }

        //delete and add images to DB
        productImageRepo.deleteAllById(imageIdsForDelete);
        productImageRepo.saveAll(newProductImages);

        //delete images from S3
        for(var deleteImageKey : imageKeysForDelete) {
            s3Service.deleteOneImage(deleteImageKey);
        }

        //add images to S3
        for(var image : imagesForAdd.entrySet()) {
            s3Service.putOneImage(image.getValue(), image.getKey());
        }
    }

    public ProductSearchResponse findAllProductBySearch(ProductSearchRequest productSearchRequest) {
        ElasticSearchResult elasticSearchResult = esService.findAllProduct(productSearchRequest);

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

    //todo: need build category tree
    public List<CategoryDTO> findAllCategoryByName(String name) {
        return categoryRepo.findAllByNameContainingIgnoreCase(name).stream()
                .map(catalogueMapper::mapToCategoryDTO)
                .toList();
    }

//    Session session = entityManager.unwrap(Session.class);
//    SessionFactory sessionFactory = session.getSessionFactory();
//    sessionFactory.getCache();
//    Session session = entityManager.unwrap(Session.class);
//    Statistics statistics = session.getSessionFactory().getStatistics();
//    CacheRegionStatistics cacheStatistics = statistics.getDomainDataRegionStatistics("static-data");

    public Filters findAllAvailableCharacteristic(Long categoryId) {
        return catalogueMapper.mapToFilters(characteristicRepo.findAllWithTextValuesByCategoryId(categoryId));
    }

    @Transactional
    //todo: when product doesn't exist, no errors will throw. FIX
    public void deleteOneProduct(Long id) {
        List<String> imageKeysForDelete = productImageRepo.findAllByProductId(id).stream()
                .map(ProductImage::getImageKey)
                .toList();

        productRepo.deleteById(id);
        esService.deleteOneProduct(id);

        for(var imageKey : imageKeysForDelete) {
            s3Service.deleteOneImage(imageKey);
        }
    }

    @Transactional
    //todo: when jakarta validation fail, document will index in ES anyway. FIX
    public void createOneProduct(ProductWritePayload productPayload) {
        if(productPayload.getProduct().getCategoryId() == null) {
            throw new RuntimeException("category_id is null");
        }

        Product createdProduct = catalogueMapper.mapToProduct(productPayload.getProduct());

        List<ProductCharacteristic> productCharacteristics = new ArrayList<>();
        createProductCharacteristics(createdProduct, productCharacteristics,
                productPayload.getCreateCharacteristics());

        productRepo.save(createdProduct);
        productCharacteristicRepo.saveAll(productCharacteristics);

        esService.createOneProduct(catalogueMapper.mapToProductDocument(createdProduct));
    }

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

        esService.createAllProduct(products.stream().map(catalogueMapper::mapToProductDocument).toList());
    }

    @Transactional
    public void updateOneProduct(ProductWritePayload productPayload, Long productId) {
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

        esService.updateOneProduct(catalogueMapper.mapToProductDocument(updatedProduct));
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