package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductImage;
import com.ak.store.catalogue.integration.ElasticService;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.integration.S3Service;
import com.ak.store.common.dto.catalogue.product.ProductFullReadDTO;
import com.ak.store.common.dto.catalogue.product.ProductImageWriteDTO;
import com.ak.store.common.payload.product.ProductWritePayload;
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

    @Transactional
    public void saveOrUpdateAllImage(ProductImageWriteDTO productImageWriteDTO) {
        var processedProductImages = productService.saveOrUpdateAllImage(productImageWriteDTO);
        s3Service.putAllImage(processedProductImages.getImagesForAdd());
        s3Service.deleteAllImage(processedProductImages.getImageKeysForDelete());
    }

    public ProductSearchResponse findAllProductBySearch(SearchProductRequest searchProductRequest) {
        var elasticSearchResult = elasticService.findAllProduct(searchProductRequest);

        ProductSearchResponse productSearchResponse = new ProductSearchResponse();
        if(elasticSearchResult.getIds().isEmpty()) {
            return productSearchResponse;
        }

        productSearchResponse.setContent(productService.findAllProduct(elasticSearchResult.getIds()));
        productSearchResponse.setSearchAfter(elasticSearchResult.getSearchAfter());

        return productSearchResponse;
    }

    public ProductFullReadDTO findOneProductById(Long id) {
        return productService.findOneProductById(id);
    }

    public SearchAvailableFiltersResponse findAllAvailableFilter(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        return elasticService.searchAvailableFilters(searchAvailableFiltersRequest);
    }

    @Transactional
    public void createAllProduct(List<ProductWritePayload> payloadList) {
        List<Product> createdProductList = productService.createAllProduct(payloadList);
        elasticService.createAllProduct(createdProductList);
    }

    @Transactional
    public void createOneProduct(ProductWritePayload payload) {
        Product createdProduct = productService.createOneProduct(payload);
        elasticService.createOneProduct(createdProduct);
    }

    @Transactional
    public void deleteOneProduct(Long id) {
        Product deletedProduct = productService.deleteOneProduct(id);
        elasticService.deleteOneProduct(id);
        s3Service.deleteAllImage(deletedProduct.getImages().stream()
                .map(ProductImage::getImageKey)
                .toList());
    }

    @Transactional
    public void updateOneProduct(ProductWritePayload productPayload, Long productId) {
        Product updatedProduct = productService.updateOneProduct(productPayload, productId);
        elasticService.updateOneProduct(updatedProduct);
    }
}
