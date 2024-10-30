package com.ak.store.product.jdbc;

import com.ak.store.common.dto.ProductFullDTO;
import com.ak.store.common.dto.ProductPreviewDTO;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<ProductFullDTO> findAll(String sort, int offset, int limit,
                                 Map<String, String> filters);

    List<?> findAll(String sort, int offset, int limit);

    ProductFullDTO findOne(Long id);

}
