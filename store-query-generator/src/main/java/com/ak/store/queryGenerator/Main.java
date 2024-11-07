package com.ak.store.queryGenerator;

import com.ak.store.common.dto.ProductFullDTO;
import com.ak.store.common.dto.ProductPreviewDTO;
import com.ak.store.common.entity.product.Product;
import org.springframework.data.util.ParsingUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        SelectQueryGenerator s = new SelectQueryGenerator(":", Product.class);
        System.out.println(s.select(ProductFullDTO.class));
    }
}
