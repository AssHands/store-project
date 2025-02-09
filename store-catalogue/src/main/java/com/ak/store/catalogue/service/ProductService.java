package com.ak.store.catalogue.service;

import com.ak.store.catalogue.integration.ElasticService;
import com.ak.store.catalogue.integration.S3Service;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.ProcessedProductImages;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.util.CatalogueMapper;
import com.ak.store.catalogue.util.ProductImageProcessor;
import com.ak.store.catalogue.validator.ProductImageValidator;
import com.ak.store.common.dto.catalogue.product.ProductFullReadDTO;
import com.ak.store.common.dto.catalogue.product.ProductImageWriteDTO;
import com.ak.store.common.dto.catalogue.product.ProductViewReadDTO;
import com.ak.store.common.dto.catalogue.product.ProductWriteDTO;
import com.ak.store.common.payload.product.ProductWritePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;

import static com.ak.store.catalogue.util.ProductImageProcessor.processProductImages;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final CatalogueMapper catalogueMapper;
    private final ProductImageValidator productImageValidator;
    private final ProductCharacteristicService productCharacteristicService;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    //todo: check sql statements. probably N + 1
    public ProductFullReadDTO findOneProductById(Long id) {
        return catalogueMapper.mapToProductFullReadDTO(
                productRepo.findById(id).orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(id))));
    }

    public ProcessedProductImages saveOrUpdateAllImage(ProductImageWriteDTO productImageDTO) {
        Product updatedProduct = productRepo.findOneWithImagesById(productImageDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(productImageDTO.getProductId())));

        productImageValidator.validate(productImageDTO, updatedProduct.getImages());

        ProcessedProductImages processedProductImages = processProductImages(productImageDTO, updatedProduct);

        updatedProduct.getImages().clear();
        updatedProduct.getImages().addAll(processedProductImages.getNewProductImages());
        productRepo.saveAndFlush(updatedProduct);

        return processedProductImages;
    }

    public List<ProductViewReadDTO> findAllProduct(List<Long> ids) {
        return productRepo.findAllViewByIdIn(ids).stream() //todo: make SORT
                .map(catalogueMapper::mapToProductViewReadDTO)
                .toList();
    }

    public Product deleteOneProduct(Long id) {
        Product product = productRepo.findOneWithImagesById(id).orElseThrow(() -> new RuntimeException("no products found"));
        productRepo.deleteById(id);
        return product;
    }

    public Product createOneProduct(ProductWritePayload productPayload) {
        Product createdProduct = catalogueMapper.mapToProduct(productPayload.getProduct());
        if (createdProduct.getCategory() == null || createdProduct.getCategory().getId() == null) {
            throw new RuntimeException("category_id is null");
        }
        productCharacteristicService.createProductCharacteristics(createdProduct, productPayload.getCreateCharacteristics());

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAndFlush(createdProduct);

        return createdProduct;
    }

    public List<Product> createAllProduct(List<ProductWritePayload> productPayloads) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productPayloads.size(); i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                productRepo.saveAllAndFlush(products);
                productRepo.clear();
            }

            Product createdProduct = catalogueMapper.mapToProduct(productPayloads.get(i).getProduct());
            if (createdProduct.getCategory() == null || createdProduct.getCategory().getId() == null) {
                throw new RuntimeException("one of the products does not have a defined category_id");
            }
            productCharacteristicService.createProductCharacteristics(createdProduct, productPayloads.get(i).getCreateCharacteristics());
            products.add(createdProduct);
        }

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAllAndFlush(products);
        return products;
    }

    public Product updateOneProduct(ProductWritePayload productPayload, Long productId) {
        Product updatedProduct = productRepo.findOneWithCharacteristicsAndCategoryById(productId)
                .orElseThrow(() -> new RuntimeException("product with id %s didnt find".formatted(productId)));

        updateProduct(updatedProduct, productPayload.getProduct());

        productCharacteristicService.createProductCharacteristics(updatedProduct, productPayload.getCreateCharacteristics());
        productCharacteristicService.updateProductCharacteristics(updatedProduct, productPayload.getUpdateCharacteristics());
        productCharacteristicService.deleteProductCharacteristics(updatedProduct, productPayload.getDeleteCharacteristics());

        //flush for immediate validation, without it, data will index in ES, even when validation failed
        productRepo.saveAndFlush(updatedProduct);

        return updatedProduct;
    }

    private void updateProduct(Product updatedProduct, ProductWriteDTO productWriteDTO) {
        if (productWriteDTO.getTitle() != null) {
            updatedProduct.setTitle(productWriteDTO.getTitle());
        }

        if (productWriteDTO.getDescription() != null) {
            updatedProduct.setDescription(productWriteDTO.getDescription());
        }

        boolean isFullPriceUpdated = false;
        if (productWriteDTO.getFullPrice() != null && productWriteDTO.getFullPrice() != updatedProduct.getFullPrice()) {
            updatedProduct.setFullPrice(productWriteDTO.getFullPrice());
            isFullPriceUpdated = true;
        }

        if (productWriteDTO.getDiscountPercentage() != null) {
            updatedProduct.setDiscountPercentage(productWriteDTO.getDiscountPercentage());
            int discount = updatedProduct.getFullPrice() * updatedProduct.getDiscountPercentage() / 100;
            int priceWithDiscount = updatedProduct.getFullPrice() - discount;
            updatedProduct.setCurrentPrice(priceWithDiscount);

        } else if (isFullPriceUpdated) {
            int discount = updatedProduct.getFullPrice() * updatedProduct.getDiscountPercentage() / 100;
            int priceWithDiscount = updatedProduct.getFullPrice() - discount;
            updatedProduct.setCurrentPrice(priceWithDiscount);
        }

        if (productWriteDTO.getCategoryId() != null
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
