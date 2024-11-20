package com.ak.store.queryGenerator;

import com.ak.store.common.dto.ProductFullDTO;
import com.ak.store.common.bentity.product.Product;

public class Main {
    public static void main(String[] args) {
        SelectQueryGenerator s = new SelectQueryGenerator(":", Product.class);
        System.out.println(s.select(ProductFullDTO.class));
    }
}
