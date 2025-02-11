package com.ak.store.catalogue.service.product;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.common.dto.catalogue.ProductWriteDTO;

public abstract class PriceCalculator {
     static public void updatePrice(Product product, ProductWriteDTO productWriteDTO) {
         if (productWriteDTO.getFullPrice() != null && productWriteDTO.getFullPrice() != product.getFullPrice()) {
             product.setFullPrice(productWriteDTO.getFullPrice());
         }

         if (productWriteDTO.getDiscountPercentage() != null) {
             product.setDiscountPercentage(productWriteDTO.getDiscountPercentage());
         }

         int discount = product.getFullPrice() * product.getDiscountPercentage() / 100;
         int priceWithDiscount = product.getFullPrice() - discount;
         product.setCurrentPrice(priceWithDiscount);
     }
}
