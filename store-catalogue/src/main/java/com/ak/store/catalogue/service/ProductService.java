package com.ak.store.catalogue.service;

import com.ak.store.catalogue.exception.NotFoundException;
import com.ak.store.catalogue.mapper.ProductMapper;
import com.ak.store.catalogue.model.command.WriteProductCommand;
import com.ak.store.catalogue.model.dto.ProductDTO;
import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.catalogue.model.entity.ProductStatus;
import com.ak.store.catalogue.model.entity.RatingSummary;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.util.ProductPriceCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    private Product findOneById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new NotFoundException("Product not found: id=" + id));
    }

    private List<Product> findAllById(List<Long> ids) {
        return productRepo.findAllById(ids);
    }

    public ProductDTO findOne(Long id) {
        return productMapper.toDTO(findOneById(id));
    }

    public List<ProductDTO> findAll(List<Long> ids) {
        return findAllById(ids).stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public Boolean isExistAll(List<Long> ids) {
        return productRepo.isExistAllByIds(ids, ids.size());
    }

    public Boolean isAvailableAll(List<Long> ids) {
        return productRepo.isAvailableAllByIds(ids, ids.size());
    }

    @Transactional
    public ProductDTO createOne(WriteProductCommand command) {
        var product = productMapper.toEntity(command);

        ProductPriceCalculator.setPrice(product, command);
        product.setStatus(ProductStatus.IN_PROGRESS);
        product.setReviewAmount(0);
        product.setRating(0f);
        product.setRatingSummary(RatingSummary.builder()
                .product(product)
                .build());

        //flush for immediate validation
        productRepo.saveAndFlush(product);
        return productMapper.toDTO(product);
    }

    @Transactional
    public ProductDTO updateOne(WriteProductCommand command) {
        var product = findOneById(command.getId());

        updateOneFromDTO(product, command);

        //flush for immediate validation
        return productMapper.toDTO(productRepo.saveAndFlush(product));
    }

    @Transactional
    public ProductDTO deleteOne(Long id) {
        var product = findOneById(id);

        product.setIsAvailable(false);
        product.setStatus(ProductStatus.DELETED);

        return productMapper.toDTO(productRepo.save(product));
    }

    private void updateOneFromDTO(Product product, WriteProductCommand request) {
        ProductPriceCalculator.setPrice(product, request);

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
