package com.ak.store.catalogue.facade;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductImage;
import com.ak.store.catalogue.integration.ElasticService;
import com.ak.store.catalogue.service.ProductService;
import com.ak.store.catalogue.integration.S3Service;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.common.model.catalogue.view.ProductRichView;
import com.ak.store.common.model.catalogue.dto.ImageDTO;
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
    private final CatalogueMapper catalogueMapper;

    @Transactional
    public void saveOrUpdateAllImage(ImageDTO imageDTO) {
        var processedProductImages = productService.saveOrUpdateAllImage(imageDTO);
        s3Service.putAllImage(processedProductImages.getImagesForAdd());
        s3Service.deleteAllImage(processedProductImages.getImageKeysForDelete());
    }

    public ProductSearchResponse findAllProductBySearch(SearchProductRequest searchProductRequest) {
        var elasticSearchResult = elasticService.findAllProduct(searchProductRequest);

        ProductSearchResponse productSearchResponse = new ProductSearchResponse();
        if(elasticSearchResult.getIds().isEmpty()) {
            return productSearchResponse;
        }

        productSearchResponse.setContent(
                productService.findAllProductView(elasticSearchResult.getIds(), searchProductRequest.getSortingType()).stream()
                        .map(catalogueMapper::mapToProductPoorView)
                        .toList()
        );
        productSearchResponse.setSearchAfter(elasticSearchResult.getSearchAfter());

        return productSearchResponse;
    }

    public ProductRichView findOneProduct(Long id) {
        return catalogueMapper.mapToProductRichView(productService.findOneProductWithAll(id));
    }

    public SearchAvailableFiltersResponse findAllAvailableFilter(SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        return elasticService.searchAvailableFilters(searchAvailableFiltersRequest);
    }

    @Transactional
    public void createAllProduct(List<ProductWritePayload> payloadList) {
        List<Product> createdProductList = productService.createAllProduct(payloadList);
        elasticService.createAllProduct(createdProductList.stream()
                .map(catalogueMapper::mapToProductDocument)
                .toList());
    }

    @Transactional
    public void createOneProduct(ProductWritePayload payload) {
        Product createdProduct = productService.createOneProduct(payload);
        elasticService.createOneProduct(catalogueMapper.mapToProductDocument(createdProduct));
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
        elasticService.updateOneProduct(catalogueMapper.mapToProductDocument(updatedProduct));
    }
}
