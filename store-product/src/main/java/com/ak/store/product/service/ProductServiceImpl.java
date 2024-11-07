package com.ak.store.product.service;

import com.ak.store.common.dto.ProductDTO;
import com.ak.store.common.payload.ProductPayload;
import com.ak.store.product.jdbc.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ProductRepository productRepository) {
        this.productDao = productDao;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> findAll(String sort, int offset, int limit,
                                    Map<String, String> filters, Class<?> clazz) {
        if(filters == null) {
            return productDao.findAll(sort, offset, limit, clazz);
        }
        return productDao.findAll(sort, offset, limit, filters, clazz);
    }

    @Override
    public ProductDTO findOneById(Long id, Class<?> clazz) {
        return productDao.findOneById(id, clazz);
    }

    @Override
    public ProductDTO updateOneById(Long id, ProductPayload updatedProduct) {
        return productDao.updateOneById(id, updatedProduct);
    }

    @Override
    public boolean deleteOneById(Long id) {
        return productDao.deleteOneById(id);
    }
}