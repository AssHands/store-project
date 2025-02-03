package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.common.payload.product.ProductWritePayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.ak.store.catalogue.TestProductFactory.createProductWithCategory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepo productRepo;

    @Mock
    private ElasticService elasticService;

    @Mock
    private S3Service s3Service;

    @Mock
    private CatalogueMapper catalogueMapper;

    @Mock
    private ProductCharacteristicService productCharacteristicService;
    @InjectMocks
    private ProductService productService;

    @Test
    public void createOneProduct_verifyMethodCallsAndReturnValue() {
        Long expectedProductId = 1L;
        ProductWritePayload payload = new ProductWritePayload();
        Product product = createProductWithCategory(1L);
        ProductDocument productDocument = mock(ProductDocument.class);

        when(catalogueMapper.mapToProduct(payload.getProduct())).thenReturn(product);
        when(productRepo.saveAndFlush(product)).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(expectedProductId);
            return savedProduct;
        });
        when(catalogueMapper.mapToProductDocument(product)).thenReturn(productDocument);

        Long actualProductId = productService.createOneProduct(payload);

        assertThat(actualProductId).isEqualTo(expectedProductId);
        verify(catalogueMapper, times(1)).mapToProduct(payload.getProduct());
        verify(productCharacteristicService, times(1))
                .addProductCharacteristics(product, payload.getCreateCharacteristics());
        verify(productRepo, times(1)).saveAndFlush(product);
        verify(catalogueMapper, times(1)).mapToProductDocument(product);
        verify(elasticService, times(1)).createOneProduct(productDocument);
    }

    @Test
    public void createOneProduct_verifyExceptionThrown() {
        ProductWritePayload payload = new ProductWritePayload();
        Product product = new Product();

        when(catalogueMapper.mapToProduct(payload.getProduct())).thenReturn(product);

        Exception thrown = assertThrows(
                RuntimeException.class,
                () -> productService.createOneProduct(payload));

        assertThat(thrown.getMessage()).isEqualTo("category_id is null");
        verify(catalogueMapper, times(1)).mapToProduct(payload.getProduct());
        verify(catalogueMapper, never()).mapToProductDocument(any());
        verifyNoInteractions(productCharacteristicService, productRepo, elasticService);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 5})
    public void createAllProduct_verifyMethodCalls_withoutClearFirstLevelCache(int batchSize) {
        ReflectionTestUtils.setField(productService, "BATCH_SIZE", batchSize);
        List<ProductWritePayload> payloadList = List.of(
                mock(ProductWritePayload.class),
                mock(ProductWritePayload.class),
                mock(ProductWritePayload.class)
        );
        List<Product> productList = List.of(
                createProductWithCategory(1L),
                createProductWithCategory(2L),
                createProductWithCategory(3L)
        );
        int payloadSize = payloadList.size();

        when(catalogueMapper.mapToProduct(any()))
                .thenAnswer(AdditionalAnswers.returnsElementsOf(productList));

        productService.createAllProduct(payloadList);

        verify(catalogueMapper, times(payloadSize)).mapToProduct(any());
        verify(productCharacteristicService, times(payloadSize)).addProductCharacteristics(any(), any());
        verify(productRepo, times(1)).saveAllAndFlush(any());
        verify(catalogueMapper, times(payloadSize)).mapToProductDocument(any());
        verify(elasticService, times(1)).createAllProduct(any());
        verify(productRepo, never()).clear();
    }

    @Test
    public void createAllProduct_verifyMethodCalls_withClearFirstLevelCache() {
        ReflectionTestUtils.setField(productService, "BATCH_SIZE", 2);
        List<ProductWritePayload> payloadList = List.of(
                mock(ProductWritePayload.class),
                mock(ProductWritePayload.class),
                mock(ProductWritePayload.class)
        );
        List<Product> productList = List.of(
                createProductWithCategory(1L),
                createProductWithCategory(2L),
                createProductWithCategory(3L)
        );
        int payloadSize = payloadList.size();

        when(catalogueMapper.mapToProduct(any()))
                .thenAnswer(AdditionalAnswers.returnsElementsOf(productList));

        productService.createAllProduct(payloadList);

        verify(catalogueMapper, times(payloadSize)).mapToProduct(any());
        verify(productCharacteristicService, times(payloadSize)).addProductCharacteristics(any(), any());
        verify(productRepo, times(1)).clear();
        verify(productRepo, times(2)).saveAllAndFlush(any());
        verify(catalogueMapper, times(payloadSize)).mapToProductDocument(any());
        verify(elasticService, times(2)).createAllProduct(any());
    }

    @Test
    public void createAllProduct_verifyExceptionThrown() {
        ReflectionTestUtils.setField(productService, "BATCH_SIZE", 5);
        List<ProductWritePayload> payloadList = List.of(
                mock(ProductWritePayload.class),
                mock(ProductWritePayload.class),
                mock(ProductWritePayload.class)
        );
        List<Product> productList = List.of(
                createProductWithCategory(1L),
                createProductWithCategory(2L),
                mock(Product.class)
        );

        when(catalogueMapper.mapToProduct(any()))
                .thenAnswer(AdditionalAnswers.returnsElementsOf(productList));

        Exception thrown = assertThrows(
                RuntimeException.class,
                () -> productService.createAllProduct(payloadList));

        assertThat(thrown.getMessage()).isEqualTo("one of the products does not have a defined category_id");
        verify(catalogueMapper, times(3)).mapToProduct(any());
        verify(productCharacteristicService, times(2)).addProductCharacteristics(any(), any());
        verifyNoInteractions(productRepo, elasticService);
    }

    @Test
    public void deleteOneProduct_verifyMethodCallsAndReturnValue() {
        long productId = 1L;
        when(productRepo.findOneWithImagesById(productId)).thenReturn(Optional.of(new Product()));

        boolean actualReturnValue = productService.deleteOneProduct(productId);

        assertThat(actualReturnValue).isTrue();
        verify(productRepo, times(1)).findOneWithImagesById(productId);
        verify(productRepo, times(1)).deleteById(productId);
        verify(s3Service, times(1)).deleteAllImage(any());
        verify(elasticService, times(1)).deleteOneProduct(productId);
    }

    @Test
    public void deleteOneProduct_verifyMethodCallsAndReturnValue_productNotExist() {
        long productId = 1L;
        when(productRepo.findOneWithImagesById(productId)).thenReturn(Optional.empty());

        boolean actualReturnValue = productService.deleteOneProduct(productId);

        assertThat(actualReturnValue).isFalse();
        verify(productRepo, times(1)).findOneWithImagesById(productId);
        verify(productRepo, never()).deleteById(anyLong());
        verifyNoInteractions(s3Service, elasticService);
    }
}
