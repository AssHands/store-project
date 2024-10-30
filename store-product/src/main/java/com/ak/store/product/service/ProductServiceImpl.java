package com.ak.store.product.service;

import com.ak.store.common.dto.ProductFullDTO;
import com.ak.store.common.dto.ProductPreviewDTO;
import com.ak.store.product.jdbc.ProductDao;
import com.ak.store.product.repository.ProductRepository;
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
    public List<ProductFullDTO> findAll(String sort, int offset, int limit,
                                            Map<String, String> filters) {
        if(filters == null) {
            return (List<ProductFullDTO>) productDao.findAll(sort, offset, limit);
        }
        return productDao.findAll(sort, offset, limit, filters);
    }

    @Override
    public ProductFullDTO findById(Long id) {
        return productDao.findOne(id);
    }
}