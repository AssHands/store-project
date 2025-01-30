package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.document.ProductDocument;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.utils.CatalogueMapper;
import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.payload.product.ProductWritePayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepo productRepo;

    @Mock
    private ElasticService elasticService;

    @Mock
    private CharacteristicRepo characteristicRepo;

    @Spy
    private CatalogueMapper catalogueMapper = new CatalogueMapper(new ModelMapper());

    @InjectMocks
    private ProductService productService;

    @Test
    public void createOneProduct_withValidData() {
        ProductWritePayload productWritePayload = ProductWritePayload.builder()
                .product(ProductWriteDTO.builder()
                        .title("product 1")
                        .description("desc 1")
                        .fullPrice(100)
                        .discountPercentage(10)
                        .categoryId(1L)
                        .build())
                .build();

        Product product = Product.builder()
                .id(1L)
                .title("product 1")
                .description("desc 1")
                .fullPrice(100)
                .discountPercentage(10)
                .currentPrice(90)
                .amountReviews(0)
                .grade(null)
                .category(Category.builder().id(1L).build())
                .build();

        when(productRepo.saveAndFlush(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(1L);
            return savedProduct;
        });

        Long productId = productService.createOneProduct(productWritePayload);

        assertThat(productId).isEqualTo(1L);
        verify(productRepo, times(1)).saveAndFlush(any(Product.class));
        verify(elasticService, times(1)).createOneProduct(any());
    }
}
