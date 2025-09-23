package com.ak.store.catalogue.service;

import com.ak.store.catalogue.mapper.ProductMapper;
import com.ak.store.catalogue.model.dto.ProductDTO;
import com.ak.store.catalogue.model.dto.write.ProductWriteDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductStatus;
import com.ak.store.catalogue.model.entity.RatingSummary;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.service.product.PriceCalculator;
import com.ak.store.catalogue.validator.service.ProductServiceValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final ProductServiceValidator productServiceValidator;

    private Product findOneById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    private List<Product> findAllById(List<Long> ids) {
        return productRepo.findAllById(ids);
    }

    public ProductDTO findOne(Long id) {
        return productMapper.toProductDTO(findOneById(id));
    }

    public List<ProductDTO> findAll(List<Long> ids) {
        return productMapper.toProductDTO(findAllById(ids));
    }

    public Boolean isExistAll(List<Long> ids) {
        return productRepo.isExistAllByIds(ids, ids.size());
    }

    public Boolean isAvailableAll(List<Long> ids) {
        return productRepo.isAvailableAllByIds(ids, ids.size());
    }

    @Transactional
    public ProductDTO createOne(ProductWriteDTO request) {
        productServiceValidator.validateCreating(request);
        var product = productMapper.toProduct(request);

        PriceCalculator.setPrice(product, request);
        product.setStatus(ProductStatus.IN_PROGRESS);
        product.setReviewAmount(0);
        product.setRating(0f);
        product.setRatingSummary(RatingSummary.builder()
                .product(product)
                .build());

        //flush for immediate validation
        productRepo.saveAndFlush(product);
        return productMapper.toProductDTO(product);
    }

    @Transactional
    public ProductDTO updateOne(Long id, ProductWriteDTO request) {
        var product = findOneById(id);

        updateOneFromDTO(product, request);

        //flush for immediate validation
        return productMapper.toProductDTO(productRepo.saveAndFlush(product));
    }

    @Transactional
    public ProductDTO deleteOne(Long id) {
        var product = findOneById(id);

        product.setIsAvailable(false);
        product.setStatus(ProductStatus.DELETED);

        return productMapper.toProductDTO(productRepo.save(product));
    }

    private void updateOneFromDTO(Product product, ProductWriteDTO request) {
        PriceCalculator.setPrice(product, request);

        if (request.getTitle() != null) {
            product.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getIsAvailable() != null) {
            product.setIsAvailable(request.getIsAvailable());
        }
        if (request.getCategoryId() != null && !product.getCategory().getId().equals(request.getCategoryId())) {
            product.setCategory(
                    Category.builder()
                            .id(request.getCategoryId())
                            .build());

            product.getCharacteristics().clear();
        }
    }
}
