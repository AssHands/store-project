package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.kafka.ProductProducerKafka;
import com.ak.store.catalogue.util.SagaBuilder;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductImage;
import com.ak.store.catalogue.integration.ElasticService;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.integration.S3Service;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.common.event.catalogue.ProductDeletedEvent;
import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.catalogue.view.ProductPrice;
import com.ak.store.common.model.catalogue.view.ProductRichView;
import com.ak.store.common.model.catalogue.dto.ImageDTO;
import com.ak.store.common.payload.catalogue.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.SearchProductRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceFacade {
    private final ProductService productService;
    private final ElasticService elasticService;
    private final S3Service s3Service;
    private final CatalogueMapper catalogueMapper;

    private final ProductProducerKafka productProducerKafka;

    @Transactional
    public Long saveOrUpdateAllImage(ImageDTO imageDTO) {
        var processedProductImages = productService.saveOrUpdateAllImage(imageDTO);

        new SagaBuilder()
                .step(() -> s3Service.putAllImage(processedProductImages.getImagesForAdd()))
                .compensate(() -> s3Service.compensatePutAllImage(processedProductImages.getImagesForAdd().keySet()))
                .step(() -> s3Service.deleteAllImage(processedProductImages.getImageKeysForDelete()))
                .compensate(() -> s3Service.compensateDeleteAllImage(processedProductImages.getImageKeysForDelete()))
                .execute();

        return imageDTO.getProductId();
    }

    public ProductSearchResponse findAllBySearch(SearchProductRequest searchProductRequest) {
        var elasticSearchResult = elasticService.findAllProduct(searchProductRequest);

        ProductSearchResponse productSearchResponse = new ProductSearchResponse();
        if (elasticSearchResult.getIds().isEmpty()) {
            return productSearchResponse;
        }

        productSearchResponse.setContent(
                productService.findAllPoor(elasticSearchResult.getIds(), searchProductRequest.getSortingType()).stream()
                        .map(catalogueMapper::mapToProductPoorView)
                        .toList()
        );
        productSearchResponse.setSearchAfter(elasticSearchResult.getSearchAfter());

        return productSearchResponse;
    }

    public ProductRichView findOneRich(Long id) {
        return catalogueMapper.mapToProductRichView(productService.findOneWithAll(id));
    }

    public ProductPoorView findOnePoor(Long id) {
        return catalogueMapper.mapToProductPoorView(productService.findOneWithImages(id));
    }

    @Transactional
    public SearchAvailableFiltersResponse findAllAvailableFilter(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        return elasticService.searchAvailableFilters(searchAvailableFiltersRequest);
    }

    @Transactional
    public void createAll(List<ProductWritePayload> payloadList) {
        List<Product> createdProductList = productService.createAll(payloadList);
        elasticService.createAllProduct(createdProductList.stream()
                .map(catalogueMapper::mapToProductDocument)
                .toList());
    }

    @Transactional
    public Long createOne(ProductWritePayload payload) {
        Product createdProduct = productService.createOne(payload);
        elasticService.createOneProduct(catalogueMapper.mapToProductDocument(createdProduct));
        return createdProduct.getId();
    }

    @Transactional
    public void deleteOne(Long id) {
        Product deletedProduct = productService.deleteOne(id);
        List<String> imageKeyList = deletedProduct.getImages().stream()
                .map(ProductImage::getImageKey)
                .toList();

        new SagaBuilder()
                .step(() -> elasticService.deleteOneProduct(id))
                .compensate(() -> elasticService.restoreOneProduct(catalogueMapper.mapToProductDocument(deletedProduct)))
                .step(() -> s3Service.deleteAllImage(imageKeyList))
                .compensate(() -> s3Service.compensateDeleteAllImage(imageKeyList))
                .step(() -> productProducerKafka.send(new ProductDeletedEvent(id)))
                .execute();
    }

    @Transactional
    public Long updateOne(ProductWritePayload productPayload, Long productId) {
        Product updatedProduct = productService.updateOne(productPayload, productId);
        elasticService.updateOneProduct(catalogueMapper.mapToProductDocument(updatedProduct));
        return updatedProduct.getId();
    }

    public List<ProductPoorView> findAllPoor(List<Long> ids) {
        return productService.findAllPoor(ids).stream()
                .map(catalogueMapper::mapToProductPoorView)
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

    public List<ProductPrice> getAllPrice(List<Long> ids) {
        return productService.findAll(ids).stream()
                .map(catalogueMapper::mapToProductPrice)
                .toList();
    }
}
