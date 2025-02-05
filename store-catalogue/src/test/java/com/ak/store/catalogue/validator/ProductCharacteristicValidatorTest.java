package com.ak.store.catalogue.validator;

import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ProductCharacteristicValidatorTest {
    ProductCharacteristicValidator productCharacteristicValidator = new ProductCharacteristicValidator();

    @Test
    public void validate_verifyNoExceptionThrown() {
        List<ProductCharacteristicDTO> productCharacteristicDTOList = List.of();
    }
}
