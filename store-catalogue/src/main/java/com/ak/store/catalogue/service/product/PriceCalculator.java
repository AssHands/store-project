package com.ak.store.catalogue.service.product;

import com.ak.store.catalogue.model.command.WriteProductCommand;
import com.ak.store.catalogue.model.entity.Product;

public abstract class PriceCalculator {
     static public void setPrice(Product product, WriteProductCommand productDTO) {
         if(productDTO.getFullPrice() != null) {
             product.setFullPrice(productDTO.getFullPrice());
         }

         if(productDTO.getDiscountPercentage() != null) {
             product.setDiscountPercentage(productDTO.getDiscountPercentage());
         }

         int discount = product.getFullPrice() * product.getDiscountPercentage() / 100;
         int priceWithDiscount = product.getFullPrice() - discount;
         product.setCurrentPrice(priceWithDiscount);
     }
}
