package com.ak.store.product.service;

import com.ak.store.common.dto.ProductDto;
import com.ak.store.product.jdbc.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<ProductDto> findAll(String sort, int offset, int limit,
                                             Map<String, String> filters) {
        if(filters == null) {
            return productDao.findAll(sort, offset, limit);
        }
        return productDao.findAll(sort, offset, limit, filters);
    }
}