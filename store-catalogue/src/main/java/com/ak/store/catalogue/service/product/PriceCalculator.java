package com.ak.store.catalogue.service.product;

import com.ak.store.catalogue.model.dto.write.ProductWriteDTO;
import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.common.model.catalogue.form.ProductForm;

public abstract class PriceCalculator {
     static public void definePrice(Product product, ProductWriteDTO productDTO) {
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
