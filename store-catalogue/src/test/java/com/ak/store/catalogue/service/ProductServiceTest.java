package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductImage;
import com.ak.store.catalogue.model.pojo.ElasticSearchResult;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.util.ProductUtils;
import com.ak.store.catalogue.validator.ProductImageValidator;
import com.ak.store.common.dto.catalogue.product.ProductFullReadDTO;
import com.ak.store.common.dto.catalogue.product.ProductViewReadDTO;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.SearchProductRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Stream;

import static com.ak.store.catalogue.TestProductFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
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

    @Mock
    private ProductUtils productUtils;

    @Mock
    private ProductImageValidator productImageValidator;

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

    @Test
    public void updateOneProduct_verifyMethodCallsAndReturnValue() {
        Long expectedProductId = 1L;
        ProductWritePayload payload = new ProductWritePayload();
        Product product = mock(Product.class);
        when(productRepo.findOneWithCharacteristicsAndCategoryById(expectedProductId)).thenReturn(Optional.of(product));

        Long actualProductId = productService.updateOneProduct(payload, expectedProductId);

        assertThat(actualProductId).isEqualTo(expectedProductId);

        verify(productRepo, times(1)).findOneWithCharacteristicsAndCategoryById(expectedProductId);
        verify(productCharacteristicService, times(1))
                .addProductCharacteristics(product, payload.getCreateCharacteristics());
        verify(productCharacteristicService, times(1))
                .updateProductCharacteristics(product, payload.getUpdateCharacteristics());
        verify(productCharacteristicService, times(1))
                .deleteProductCharacteristics(product, payload.getDeleteCharacteristics());
        verify(productRepo, times(1)).saveAndFlush(product);
        verify(catalogueMapper, times(1)).mapToProductDocument(product);
        verify(elasticService, times(1)).updateOneProduct(any());
    }

    static Stream<Object[]> updateOneProduct_provideTestCases() {
        return Stream.of(
                new Object[]{200, null},
                new Object[]{null, 10}
        );
    }

    @ParameterizedTest
    @MethodSource("updateOneProduct_provideTestCases")
    public void updateOneProduct_verifyUpdatingData(Integer fullPrice, Integer discountPercentage) {
        ProductWritePayload payload = createProductPayload("update title", "update desc", fullPrice, discountPercentage, 3L);
        Product product = createProduct(1L, "title", "desc", 100, 20, 80, 2L);
        when(productRepo.findOneWithCharacteristicsAndCategoryById(1L)).thenReturn(Optional.of(product));

        productService.updateOneProduct(payload, 1L);

        if (fullPrice != null && discountPercentage == null) {
            assertThat(product.getFullPrice()).isEqualTo(200);
            assertThat(product.getCurrentPrice()).isEqualTo(160);
            assertThat(product.getDiscountPercentage()).isEqualTo(20);
        } else if (fullPrice == null && discountPercentage != null) {
            assertThat(product.getFullPrice()).isEqualTo(100);
            assertThat(product.getCurrentPrice()).isEqualTo(90);
            assertThat(product.getDiscountPercentage()).isEqualTo(10);
        }

        assertThat(product.getTitle()).isEqualTo("update title");
        assertThat(product.getDescription()).isEqualTo("update desc");
        assertThat(product.getCategory().getId()).isEqualTo(3L);
    }

    @Test
    public void updateOneProduct_verifyExceptionThrown() {
        Long productId = 1L;
        when(productRepo.findOneWithCharacteristicsAndCategoryById(productId)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(
                RuntimeException.class,
                () -> productService.updateOneProduct(mock(ProductWritePayload.class), productId));
        assertThat(thrown.getMessage()).isEqualTo("product with id 1 didnt find");

        verify(productRepo, times(1)).findOneWithCharacteristicsAndCategoryById(productId);
        verify(productRepo, never()).saveAndFlush(any());
        verifyNoInteractions(productCharacteristicService, elasticService);
    }

    @Test
    public void findOneProductById_verifyMethodCallsAndReturnValue() {
        Long productId = 1L;
        Product mockProduct = mock(Product.class);
        ProductFullReadDTO expectedProduct = ProductFullReadDTO.builder().id(productId).build();
        when(productRepo.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(catalogueMapper.mapToProductFullReadDTO(mockProduct)).thenReturn(expectedProduct);

        ProductFullReadDTO actualProduct = productService.findOneProductById(productId);

        assertThat(actualProduct).isEqualTo(expectedProduct);
        verify(productRepo, times(1)).findById(productId);
        verify(catalogueMapper, times(1)).mapToProductFullReadDTO(any());
    }

    @Test
    public void findOneProductById_verifyExceptionThrown() {
        Long productId = 1L;
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(
                RuntimeException.class,
                () -> productService.findOneProductById(productId));

        assertThat(thrown.getMessage()).isEqualTo("product with id 1 didnt find");
        verify(productRepo, times(1)).findById(productId);
        verifyNoInteractions(catalogueMapper);
    }

    @Test
    public void findAllAvailableFilter_verifyMethodCallsAndReturnValue() {
        SearchAvailableFiltersResponse expectedResponse = mock(SearchAvailableFiltersResponse.class);
        SearchAvailableFiltersRequest mockRequest = mock(SearchAvailableFiltersRequest.class);
        when(elasticService.searchAvailableFilters(mockRequest)).thenReturn(expectedResponse);

        SearchAvailableFiltersResponse actualResponse = productService.findAllAvailableFilter(mockRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(elasticService, times(1)).searchAvailableFilters(mockRequest);
    }

    @Test
    public void findAllProductBySearch_verifyMethodCallsAndReturnValue() {
        List<ProductViewReadDTO> productDTOList = List.of(
                mock(ProductViewReadDTO.class),
                mock(ProductViewReadDTO.class),
                mock(ProductViewReadDTO.class)
        );
        List<Product> productList = List.of(
                mock(Product.class),
                mock(Product.class),
                mock(Product.class)
        );
        List<Long> ids = List.of(1L, 2L, 3L);
        ElasticSearchResult elasticSearchResult = ElasticSearchResult.builder()
                .ids(ids)
                .searchAfter(List.of(123, "123"))
                .build();
        ProductSearchResponse expectedResponse = ProductSearchResponse.builder()
                .content(productDTOList)
                .searchAfter(List.of(123, "123"))
                .build();

        when(elasticService.findAllProduct(any(SearchProductRequest.class))).thenReturn(elasticSearchResult);
        when(productRepo.findAllViewByIdIn(ids)).thenReturn(productList);
        when(catalogueMapper.mapToProductViewReadDTO(any()))
                .thenAnswer(AdditionalAnswers.returnsElementsOf(productDTOList));

        ProductSearchResponse actualResponse = productService.findAllProductBySearch(mock(SearchProductRequest.class));

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(elasticService, times(1)).findAllProduct(any());
        verify(productRepo, times(1)).findAllViewByIdIn(any());
        verify(catalogueMapper, times(productList.size())).mapToProductViewReadDTO(any());
    }

    @Test
    public void findAllProductBySearch_verifyExceptionThrown() {
        when(elasticService.findAllProduct(any())).thenReturn(null);

        Exception thrown = assertThrows(
                RuntimeException.class,
                () -> productService.findAllProductBySearch(mock(SearchProductRequest.class)));

        assertThat(thrown.getMessage()).isEqualTo("no documents found");
        verify(elasticService, times(1)).findAllProduct(any());
        verifyNoInteractions(catalogueMapper, productRepo);
    }

    @Test
    public void saveOrUpdateAllImage_verifyMethodCallsAndReturnValue() {
        Long expectedProductId = 1L;
        Product product = new Product();
        LinkedHashMap<String, MultipartFile> imagesForAdd = new LinkedHashMap<>(Map.of(
                "key 1", mock(MultipartFile.class),
                "key 2", mock(MultipartFile.class),
                "key 3", mock(MultipartFile.class)
        ));
        List<ProductImage> productImageList = List.of(
                createProductImage(0, "key 1"),
                createProductImage(1, "key 2"),
                createProductImage(2, "key 3")
        );

        when(productRepo.findOneWithImagesById(anyLong())).thenReturn(Optional.of(product));
        when(productUtils.markImagesForDelete(any(), any())).thenReturn(Collections.emptyList());
        when(productUtils.prepareImagesForAdd(any(), any())).thenReturn(imagesForAdd);
        when(productUtils.createNewProductImagesList(any(), any())).thenReturn(productImageList);

        Long actualProductId = productService.saveOrUpdateAllImage(expectedProductId, Collections.emptyMap(),
                Collections.emptyList(), Collections.emptyList());

        assertThat(actualProductId).isEqualTo(expectedProductId);
        verify(productRepo, times(1)).findOneWithImagesById(anyLong());
        verify(productImageValidator, times(1)).validate(any(), any(), any(), any());
        verify(productUtils, times(1)).markImagesForDelete(any(), any());
        verify(productUtils, times(1)).prepareImagesForAdd(any(), any());
        verify(productUtils, times(1)).createNewProductImagesList(any(), any());
        verify(productRepo, times(1)).saveAndFlush(any());
        verify(s3Service, times(1)).deleteAllImage(any());
        verify(s3Service, times(1)).putAllImage(any());
    }

    @Test
    public void saveOrUpdateAllImage_verifyExceptionThrown() {
        when(productRepo.findOneWithImagesById(anyLong())).thenReturn(Optional.empty());

        Exception thrown = assertThrows(
                RuntimeException.class,
                () -> productService.saveOrUpdateAllImage(1L, Collections.emptyMap(),
                        Collections.emptyList(), Collections.emptyList()));

        assertThat(thrown.getMessage()).isEqualTo("product with id 1 didnt find");

        verify(productRepo, times(1)).findOneWithImagesById(anyLong());
        verify(productRepo, times(0)).saveAndFlush(any());
        verifyNoInteractions(productImageValidator, productUtils, s3Service);
    }
}
