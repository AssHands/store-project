package com.ak.store.catalogue.service.product;

import com.ak.store.catalogue.model.entity.Product;
import com.ak.store.common.model.catalogue.form.ProductForm;

public abstract class PriceCalculator {
     static public void updatePrice(Product product, ProductForm productForm) {
         if (productForm.getFullPrice() != null && productForm.getFullPrice() != product.getFullPrice()) {
             product.setFullPrice(productForm.getFullPrice());
         }

         if (productForm.getDiscountPercentage() != null) {
             product.setDiscountPercentage(productForm.getDiscountPercentage());
         }

         int discount = product.getFullPrice() * product.getDiscountPercentage() / 100;
         int priceWithDiscount = product.getFullPrice() - discount;
         product.setCurrentPrice(priceWithDiscount);
     }
}
