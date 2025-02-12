package com.ak.store.catalogue.service.product;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.common.model.catalogue.dto.ProductDTO;

public abstract class PriceCalculator {
     static public void updatePrice(Product product, ProductDTO productDTO) {
         if (productDTO.getFullPrice() != null && productDTO.getFullPrice() != product.getFullPrice()) {
             product.setFullPrice(productDTO.getFullPrice());
         }

         if (productDTO.getDiscountPercentage() != null) {
             product.setDiscountPercentage(productDTO.getDiscountPercentage());
         }

         int discount = product.getFullPrice() * product.getDiscountPercentage() / 100;
         int priceWithDiscount = product.getFullPrice() - discount;
         product.setCurrentPrice(priceWithDiscount);
     }
}
