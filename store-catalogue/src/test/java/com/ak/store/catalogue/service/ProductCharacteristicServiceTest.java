//package com.ak.store.catalogue.service;
//
//import com.ak.store.catalogue.model.entity.Product;
//import com.ak.store.catalogue.model.entity.ProductCharacteristic;
//import com.ak.store.catalogue.repository.CharacteristicRepo;
//import com.ak.store.catalogue.util.CatalogueMapper;
//import com.ak.store.catalogue.validator.ProductCharacteristicValidator;
//import com.ak.store.common.dto.catalogue.ProductCharacteristicDTO;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.AdditionalAnswers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static com.ak.store.catalogue.TestProductFactory.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ProductCharacteristicServiceTest {
//    @Mock
//    private CatalogueMapper catalogueMapper;
//
//    @Mock
//    private CharacteristicRepo characteristicRepo;
//
//    @Mock
//    private ProductCharacteristicValidator productCharacteristicValidator;
//
//    @InjectMocks
//    private ProductCharacteristicService productCharacteristicService;
//
//    @Test
//    public void createProductCharacteristics_verifyMethodCallsAndResult() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, "test 3")))
//        );
//        Set<ProductCharacteristicDTO> createProductCharacteristicDTOSet = Set.of(
//                createProductCharacteristicDTO(4L, "test 4"),
//                createProductCharacteristicDTO(5L, 5)
//        );
//        List<ProductCharacteristic> newProductCharacteristicList = List.of(
//                createProductCharacteristic(4L, "test 4"),
//                createProductCharacteristic(5L, 5)
//        );
//
//        List<ProductCharacteristic> expectedProductCharacteristicList = List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, "test 3"),
//                createProductCharacteristic(4L, "test 4"),
//                createProductCharacteristic(5L, 5)
//        );
//
//        when(catalogueMapper.mapToProductCharacteristic(any(), any()))
//                .thenAnswer(AdditionalAnswers.returnsElementsOf(newProductCharacteristicList));
//
//        productCharacteristicService.createProductCharacteristics(product, createProductCharacteristicDTOSet);
//
//        assertThat(product.getCharacteristics()).isEqualTo(expectedProductCharacteristicList);
//        verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(any());
//        verify(productCharacteristicValidator, times(1)).validate(any(), any());
//        verify(catalogueMapper, times(newProductCharacteristicList.size())).mapToProductCharacteristic(any(), any());
//    }
//
//    @Test
//    public void createProductCharacteristics_verifyMethodCallsAndResult_emptyCollection() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, "test 3")))
//        );
//        Set<ProductCharacteristicDTO> createProductCharacteristicDTOSet = Collections.emptySet();
//
//        List<ProductCharacteristic> expectedProductCharacteristicList = List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, "test 3")
//        );
//
//        productCharacteristicService.createProductCharacteristics(product, createProductCharacteristicDTOSet);
//
//        assertThat(product.getCharacteristics()).isEqualTo(expectedProductCharacteristicList);
//        verifyNoInteractions(characteristicRepo, productCharacteristicValidator, catalogueMapper);
//    }
//
//    @Test
//    public void createProductCharacteristics_verifyExceptionThrown() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, "test 3")))
//        );
//        Set<ProductCharacteristicDTO> createProductCharacteristicDTOSet = Set.of(
//                createProductCharacteristicDTO(4L, "test 4"),
//                createProductCharacteristicDTO(1L, "test 5")
//        );
//
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productCharacteristicService.createProductCharacteristics(product, createProductCharacteristicDTOSet));
//
//        assertThat(thrown.getMessage()).isEqualTo("characteristic with id=1 already exists");
//        verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(any());
//        verify(productCharacteristicValidator, times(1)).validate(any(), any());
//    }
//
//    @Test
//    public void updateProductCharacteristics_verifyMethodCallsAndResult() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, 3)))
//        );
//        Set<ProductCharacteristicDTO> updateProductCharacteristicDTOSet = Set.of(
//                createProductCharacteristicDTO(2L, "test 4"),
//                createProductCharacteristicDTO(3L, 5)
//        );
//
//        List<ProductCharacteristic> expectedProductCharacteristicList = List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 4"),
//                createProductCharacteristic(3L, 5)
//        );
//
//        productCharacteristicService.updateProductCharacteristics(product, updateProductCharacteristicDTOSet);
//
//        assertThat(product.getCharacteristics()).isEqualTo(expectedProductCharacteristicList);
//        verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(any());
//        verify(productCharacteristicValidator, times(1)).validate(any(), any());
//    }
//
//    @Test
//    public void updateProductCharacteristics_verifyMethodCallsAndResult_emptyCollection() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, 3)))
//        );
//
//        Set<ProductCharacteristicDTO> updateProductCharacteristicDTOSet = Collections.emptySet();
//
//        productCharacteristicService.updateProductCharacteristics(product, updateProductCharacteristicDTOSet);
//
//        verifyNoInteractions(characteristicRepo, productCharacteristicValidator);
//    }
//
//    @Test
//    public void updateProductCharacteristics_verifyExceptionThrown() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, 3)
//        )
//        ));
//        Set<ProductCharacteristicDTO> updateProductCharacteristicDTOSet = Set.of(
//                createProductCharacteristicDTO(4L, "test 4")
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productCharacteristicService.updateProductCharacteristics(product, updateProductCharacteristicDTOSet));
//
//        assertThat(thrown.getMessage()).isEqualTo("characteristic with id=4 didn't find in your product");
//
//        verify(characteristicRepo, times(1)).findAllWithTextValuesByCategoryId(any());
//        verify(productCharacteristicValidator, times(1)).validate(any(), any());
//    }
//
//    @Test
//    public void deleteProductCharacteristics_verifyMethodCallsAndResult() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, 3)))
//        );
//        Set<ProductCharacteristicDTO> deleteProductCharacteristicDTOSet = Set.of(
//                ProductCharacteristicDTO.builder().id(2L).build(),
//                ProductCharacteristicDTO.builder().id(3L).build()
//        );
//
//        List<ProductCharacteristic> expectedProductCharacteristicList = List.of(
//                createProductCharacteristic(1L, "test 1")
//        );
//
//        productCharacteristicService.deleteProductCharacteristics(product, deleteProductCharacteristicDTOSet);
//
//        assertThat(product.getCharacteristics()).isEqualTo(expectedProductCharacteristicList);
//    }
//
//    @Test
//    public void deleteProductCharacteristics_verifyMethodCallsAndResult_emptyCollection() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, 3)))
//        );
//        Set<ProductCharacteristicDTO> deleteProductCharacteristicDTOSet = Collections.emptySet();
//        List<ProductCharacteristic> expectedProductCharacteristicList = List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, 3)
//        );
//
//        productCharacteristicService.deleteProductCharacteristics(product, deleteProductCharacteristicDTOSet);
//
//        assertThat(product.getCharacteristics()).isEqualTo(expectedProductCharacteristicList);
//    }
//
//    @Test
//    public void deleteProductCharacteristics_verifyExceptionThrown() {
//        Product product = createProduct(1L, 1L, new ArrayList<>(List.of(
//                createProductCharacteristic(1L, "test 1"),
//                createProductCharacteristic(2L, "test 2"),
//                createProductCharacteristic(3L, 3)))
//        );
//        Set<ProductCharacteristicDTO> deleteProductCharacteristicDTOSet = Set.of(
//                ProductCharacteristicDTO.builder().id(4L).build()
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productCharacteristicService.deleteProductCharacteristics(product, deleteProductCharacteristicDTOSet));
//
//        assertThat(thrown.getMessage()).isEqualTo("characteristic with id=4 didn't find in your product");
//    }
//}
