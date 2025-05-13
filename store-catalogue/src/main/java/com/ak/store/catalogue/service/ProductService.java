package com.ak.store.catalogue.service;

import com.ak.store.catalogue.model.dto.write.ProductWriteDTO;
import com.ak.store.catalogue.model.dto.ProductDTOnew;
import com.ak.store.catalogue.model.entity.*;
import com.ak.store.catalogue.model.pojo.ProcessedImages;
import com.ak.store.catalogue.repository.ProductRepo;
import com.ak.store.catalogue.service.product.PriceCalculator;
import com.ak.store.catalogue.util.mapper.ProductMapper;
import com.ak.store.catalogue.validator.service.ProductServiceValidator;
import com.ak.store.catalogue.validator.ImageValidator;
import com.ak.store.common.model.catalogue.form.ImageForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final ProductServiceValidator productServiceValidator;

    public List<Product> findAllOld(List<Long> ids) {
        return productRepo.findAllById(ids);
    }

    //-----------------------------

    private Product findOneById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
    }

    private List<Product> findAllById(List<Long> ids) {
        return productRepo.findAllById(ids);
    }

    public ProductDTOnew findOne(Long id) {
        return productMapper.toProductDTOnew(findOneById(id));
    }

    public List<ProductDTOnew> findAll(List<Long> ids) {
        return productMapper.toProductDTOnew(findAllById(ids));
    }

    public Boolean isExistAll(List<Long> ids) {
        return productRepo.isExistAllByIds(ids, ids.size());
    }

    public Boolean isAvailableAll(List<Long> ids) {
        return productRepo.isAvailableAllByIds(ids, ids.size());
    }

    @Transactional
    public ProductDTOnew createOne(ProductWriteDTO request) {
        productServiceValidator.validateCreation(request);
        Product product = productMapper.toProduct(request);

        PriceCalculator.definePrice(product, request);
        product.setIsDeleted(false);

        //flush for immediate validation
        productRepo.saveAndFlush(product);
        return productMapper.toProductDTOnew(product);
    }

    @Transactional
    public ProductDTOnew updateOne(Long id, ProductWriteDTO request) {
        Product product = findOneById(id);

        updateOneFromDTO(product, request);

        //flush for immediate validation
        return productMapper.toProductDTOnew(productRepo.saveAndFlush(product));
    }

    @Transactional
    public ProductDTOnew deleteOne(Long id) {
        Product product = findOneById(id);

        product.setIsAvailable(false);
        product.setIsDeleted(true);

        return productMapper.toProductDTOnew(productRepo.save(product));
    }

    private void updateOneFromDTO(Product product, ProductWriteDTO request) {
        PriceCalculator.definePrice(product, request);

        if (request.getTitle() != null) {
            product.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if(request.getIsAvailable() != null) {
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
