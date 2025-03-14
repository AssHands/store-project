package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.outbox.OutboxTaskService;
import com.ak.store.catalogue.outbox.OutboxTaskType;
import com.ak.store.catalogue.util.mapper.ProductMapper;
import com.ak.store.catalogue.util.SagaBuilder;
import com.ak.store.catalogue.model.entity.ProductImage;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.integration.S3Service;
import com.ak.store.common.model.catalogue.dto.ProductDTO;
import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.catalogue.dto.ProductPriceDTO;
import com.ak.store.common.model.catalogue.view.ProductRichView;
import com.ak.store.common.model.catalogue.form.ImageForm;
import com.ak.store.common.payload.catalogue.ProductWritePayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFacade {
    private final ProductService productService;
    private final S3Service s3Service;
    private final ProductMapper productMapper;
    private final OutboxTaskService<ProductDTO> outboxTaskService;

    @Transactional
    public Long saveOrUpdateAllImage(ImageForm imageForm) {
        var processedProductImages = productService.saveOrUpdateAllImage(imageForm);
        var product = productService.findOneWithImages(imageForm.getProductId());
        outboxTaskService.createOneTask(productMapper.toProductDTO(product), OutboxTaskType.PRODUCT_UPDATED);

        new SagaBuilder()
                .step(() -> s3Service.putAllImage(processedProductImages.getImagesForAdd()))
                .compensate(() -> s3Service.compensatePutAllImage(processedProductImages.getImagesForAdd().keySet()))
                .step(() -> s3Service.deleteAllImage(processedProductImages.getImageKeysForDelete()))
                .compensate(() -> s3Service.compensateDeleteAllImage(processedProductImages.getImageKeysForDelete()))
                .execute();

        return product.getId();
    }

    @Transactional
    public Long createOne(ProductWritePayload payload) {
        var product = productService.createOne(payload);
        outboxTaskService.createOneTask(productMapper.toProductDTO(product), OutboxTaskType.PRODUCT_CREATED);
        return product.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        var product = productService.deleteOne(id);
        outboxTaskService.createOneTask(productMapper.toProductDTO(product), OutboxTaskType.PRODUCT_DELETED);

        List<String> imageKeyList = product.getImages().stream()
                .map(ProductImage::getKey)
                .toList();

        new SagaBuilder()
                .step(() -> s3Service.deleteAllImage(imageKeyList))
                .compensate(() -> s3Service.compensateDeleteAllImage(imageKeyList))
                .execute();
    }

    @Transactional
    public Long updateOne(ProductWritePayload productPayload, Long productId) {
        var product = productService.updateOne(productPayload, productId);
        outboxTaskService.createOneTask(productMapper.toProductDTO(product), OutboxTaskType.PRODUCT_UPDATED);

        return product.getId();
    }

    public ProductRichView findOneRich(Long id) {
        return productMapper.toProductRichView(productService.findOneWithAll(id));
    }

    public ProductPoorView findOnePoor(Long id) {
        return productMapper.toProductPoorView(productService.findOneWithImages(id));
    }

    public List<ProductPoorView> findAllPoor(List<Long> ids) {
        return productService.findAllPoor(ids).stream()
                .map(productMapper::toProductPoorView)
                .toList();
    }

    public Boolean existOne(Long id) {
        return productService.existOne(id);
    }

    public Boolean availableOne(Long id) {
        return productService.availableOne(id);
    }

    public Boolean availableAll(List<Long> ids) {
        return productService.availableAll(ids);
    }

    public List<ProductPriceDTO> getAllPrice(List<Long> ids) {
        return productService.findAll(ids).stream()
                .map(productMapper::toProductPriceDTO)
                .toList();
    }

    //    @Transactional
//    public SearchAvailableFiltersResponse findAllAvailableFilter(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
//        return elasticService.searchAvailableFilters(searchAvailableFiltersRequest);
//    }

//    public ProductSearchResponse findAllBySearch(String consumerId, SearchProductRequest searchProductRequest) {
//        var elasticSearchResult = elasticService.findAllProduct(searchProductRequest);
//
//        List<ProductPoorView> productPoorViewList = new ArrayList<>(
//                elasticSearchResult.getContent().stream()
//                        .map(catalogueMapper0::mapToProductPoorView)
//                        .toList()
//        );
//
//        return ProductSearchResponse.builder()
//                .content(productPoorViewList)
//                .searchAfter(elasticSearchResult.getSearchAfter())
//                .build();
//    }
}
