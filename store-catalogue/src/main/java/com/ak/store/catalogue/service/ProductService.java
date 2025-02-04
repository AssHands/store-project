package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.catalogue.utils.ProductUtils;
import com.ak.store.catalogue.validator.ProductImageValidator;
import com.ak.store.common.dto.catalogue.product.ProductFullReadDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.SearchProductRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ElasticService elasticService;
    private final CatalogueMapper catalogueMapper;
    private final ProductImageValidator productImageValidator;
    private final S3Service s3Service;
    private final ProductUtils productUtils;
    private final ProductCharacteristicService productCharacteristicService;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    //todo: check sql statements. probably N + 1
    public ProductFullReadDTO findOneProductById(Long id) {
        return catalogueMapper.mapToProductFullReadDTO(
                productRepo.findById(id).orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(id))));
    }

    @Transactional
    public Long saveOrUpdateAllImage(Long productId, Map<String, String> allImageIndexes,
                                     List<MultipartFile> addImages, List<String> deleteImageIndexes) {
        Product updatedProduct = productRepo.findOneWithImagesById(productId)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(productId)));

        List<ProductImage> productImages = updatedProduct.getImages();
        productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages);

        List<String> imageKeysForDelete = productUtils.markImagesForDelete(productImages, deleteImageIndexes);
        //LinkedHashMap for save order
        LinkedHashMap<String, MultipartFile> imagesForAdd = productUtils.prepareImagesForAdd(updatedProduct, addImages);

        for (String key : imagesForAdd.keySet()) {
            productImages.add(ProductImage.builder()
                    .imageKey(key)
                    .product(Product.builder()
                            .id(productId)
                            .build())
                    .build());
        }

        List<ProductImage> newProductImages = productUtils.createNewProductImagesList(productImages, allImageIndexes);

        //update images in DB
        updatedProduct.getImages().clear();
        updatedProduct.getImages().addAll(newProductImages);
        productRepo.saveAndFlush(updatedProduct);

        //update images in S3
        s3Service.deleteAllImage(imageKeysForDelete);
        s3Service.putAllImage(imagesForAdd);

        return productId;
    }

    public ProductSearchResponse findAllProductBySearch(SearchProductRequest searchProductRequest) {
        ElasticSearchResult elasticSearchResult = elasticService.findAllProduct(searchProductRequest);

        if(elasticSearchResult == null) {
            throw new RuntimeException("no documents found");
        }

        ProductSearchResponse productSearchResponse = new ProductSearchResponse();

        productSearchResponse.setContent(
                productRepo.findAllViewByIdIn(elasticSearchResult.getIds()).stream() //todo: make SORT
                        .map(catalogueMapper::mapToProductViewReadDTO)
                        .toList());

        productSearchResponse.setSearchAfter(elasticSearchResult.getSearchAfter());

        return productSearchResponse;
    }

    public SearchAvailableFiltersResponse findAllAvailableFilter(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        return elasticService.searchAvailableFilters(searchAvailableFiltersRequest);
    }

    @Transactional
    //todo: when product doesn't exist, no errors will throw. MAKE IT
    public boolean deleteOneProduct(Long id) {
        Optional<Product> product = productRepo.findOneWithImagesById(id);
        if(product.isPresent()) {
            List<String> imageKeys = product.get().getImages().stream()
                    .map(ProductImage::getImageKey)
                    .toList();

            productRepo.deleteById(id);
            s3Service.deleteAllImage(imageKeys);
            elasticService.deleteOneProduct(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Long createOneProduct(ProductWritePayload productPayload) {
        Product createdProduct = catalogueMapper.mapToProduct(productPayload.getProduct());
        if(createdProduct.getCategory() == null || createdProduct.getCategory().getId() == null) {
            throw new RuntimeException("category_id is null");
        }
        productCharacteristicService.addProductCharacteristics(createdProduct, productPayload.getCreateCharacteristics());

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAndFlush(createdProduct);
        elasticService.createOneProduct(catalogueMapper.mapToProductDocument(createdProduct));

        return createdProduct.getId();
    }

    @Transactional
    public void createAllProduct(List<ProductWritePayload> productPayloads) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productPayloads.size(); i++) {
            if(i > 0 && i % BATCH_SIZE == 0) {
                productRepo.saveAllAndFlush(products);
                elasticService.createAllProduct(products.stream()
                        .map(catalogueMapper::mapToProductDocument)
                        .toList());

                productRepo.clear();
                products.clear();
            }

            Product createdProduct = catalogueMapper.mapToProduct(productPayloads.get(i).getProduct());
            if(createdProduct.getCategory() == null || createdProduct.getCategory().getId() == null) {
                throw new RuntimeException("one of the products does not have a defined category_id");
            }
            productCharacteristicService.addProductCharacteristics(createdProduct, productPayloads.get(i).getCreateCharacteristics());
            products.add(createdProduct);
        }

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAllAndFlush(products);
        elasticService.createAllProduct(products.stream()
                .map(catalogueMapper::mapToProductDocument)
                .toList());
    }

    @Transactional
    public Long updateOneProduct(ProductWritePayload productPayload, Long productId) {
        Product updatedProduct = productRepo.findOneWithCharacteristicsAndCategoryById(productId)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(productId)));

        updateProduct(updatedProduct, productPayload.getProduct());

        productCharacteristicService.addProductCharacteristics(updatedProduct, productPayload.getCreateCharacteristics());
        productCharacteristicService.updateProductCharacteristics(updatedProduct, productPayload.getUpdateCharacteristics());
        productCharacteristicService.deleteProductCharacteristics(updatedProduct, productPayload.getDeleteCharacteristics());

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAndFlush(updatedProduct);
        elasticService.updateOneProduct(catalogueMapper.mapToProductDocument(updatedProduct));

        return productId;
    }

    private void updateProduct(Product updatedProduct, ProductWriteDTO productWriteDTO) {
        if(productWriteDTO.getTitle() != null) {
            updatedProduct.setTitle(productWriteDTO.getTitle());
        }

        if(productWriteDTO.getDescription() != null) {
            updatedProduct.setDescription(productWriteDTO.getDescription());
        }

        boolean isFullPriceUpdated = false;
        if(productWriteDTO.getFullPrice() != null && productWriteDTO.getFullPrice() != updatedProduct.getFullPrice()) {
            updatedProduct.setFullPrice(productWriteDTO.getFullPrice());
            isFullPriceUpdated = true;
        }

        if(productWriteDTO.getDiscountPercentage() != null) {
            updatedProduct.setDiscountPercentage(productWriteDTO.getDiscountPercentage());
            int discount = updatedProduct.getFullPrice() * updatedProduct.getDiscountPercentage() / 100;
            int priceWithDiscount = updatedProduct.getFullPrice() - discount;
            updatedProduct.setCurrentPrice(priceWithDiscount);

        } else if(isFullPriceUpdated) {
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
}
