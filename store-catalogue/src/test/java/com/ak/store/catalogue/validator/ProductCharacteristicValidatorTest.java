//package com.ak.store.catalogue.validator;
//
//import com.ak.store.catalogue.model.entity.Characteristic;
//import com.ak.store.common.dto.catalogue.write.ProductCharacteristicDTO;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.ak.store.catalogue.TestCatalogueFactory.createCharacteristic;
//import static com.ak.store.catalogue.TestProductFactory.createProductCharacteristicDTO;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//
//public class ProductCharacteristicValidatorTest {
//    ProductCharacteristicValidator productCharacteristicValidator = new ProductCharacteristicValidator();
//
//    @Test
//    public void validate_verifyNoExceptionThrown() {
//        List<ProductCharacteristicDTO> productCharacteristicDTOList = List.of(
//                createProductCharacteristicDTO(1L, "char val 1"),
//                createProductCharacteristicDTO(2L, "char val 4"),
//                createProductCharacteristicDTO(3L, 1)
//        );
//        List<Characteristic> availableCharacteristicList = List.of(
//                createCharacteristic(1L, "char 1", true, List.of("char val 1", "char val 2")),
//                createCharacteristic(2L, "char 2", true, List.of("char val 3", "char val 4")),
//                createCharacteristic(3L, "char 3", false)
//        );
//
//        productCharacteristicValidator.validate(productCharacteristicDTOList, availableCharacteristicList);
//    }
//
//    @Test
//    public void validate_verifyExceptionThrown_characteristicNotAvailable() {
//        List<ProductCharacteristicDTO> productCharacteristicDTOList = List.of(
//                createProductCharacteristicDTO(1L, "char val 1"),
//                createProductCharacteristicDTO(4L, "char val 4"),
//                createProductCharacteristicDTO(3L, 1)
//        );
//        List<Characteristic> availableCharacteristicList = List.of(
//                createCharacteristic(1L, "char 1", true, List.of("char val 1", "char val 2")),
//                createCharacteristic(2L, "char 2", true, List.of("char val 3", "char val 4")),
//                createCharacteristic(3L, "char 3", false)
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productCharacteristicValidator.validate(productCharacteristicDTOList, availableCharacteristicList));
//
//        assertThat(thrown.getMessage()).isEqualTo("characteristic with id=4 is not available");
//    }
//
//    @Test
//    public void validate_verifyExceptionThrown_productCharacteristicHasBothValues() {
//        List<ProductCharacteristicDTO> productCharacteristicDTOList = List.of(
//                createProductCharacteristicDTO(1L, "char val 1"),
//                createProductCharacteristicDTO(2L, 1, "test"),
//                createProductCharacteristicDTO(3L, 1)
//        );
//        List<Characteristic> availableCharacteristicList = List.of(
//                createCharacteristic(1L, "char 1", true, List.of("char val 1", "char val 2")),
//                createCharacteristic(2L, "char 2", true, List.of("char val 3", "char val 4")),
//                createCharacteristic(3L, "char 3", false)
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productCharacteristicValidator.validate(productCharacteristicDTOList, availableCharacteristicList));
//
//        assertThat(thrown.getMessage()).isEqualTo("characteristic with id=2 has both text value and numeric value");
//    }
//
//    @Test
//    public void validate_verifyExceptionThrown_characteristicNotText() {
//        List<ProductCharacteristicDTO> productCharacteristicDTOList = List.of(
//                createProductCharacteristicDTO(1L, "char val 1"),
//                createProductCharacteristicDTO(2L, "char val 4"),
//                createProductCharacteristicDTO(3L, "test")
//        );
//        List<Characteristic> availableCharacteristicList = List.of(
//                createCharacteristic(1L, "char 1", true, List.of("char val 1", "char val 2")),
//                createCharacteristic(2L, "char 2", true, List.of("char val 3", "char val 4")),
//                createCharacteristic(3L, "char 3", false)
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productCharacteristicValidator.validate(productCharacteristicDTOList, availableCharacteristicList));
//
//        assertThat(thrown.getMessage()).isEqualTo("characteristic with id=3 is not a text one");
//    }
//
//    @Test
//    public void validate_verifyExceptionThrown_characteristicNotNumeric() {
//        List<ProductCharacteristicDTO> productCharacteristicDTOList = List.of(
//                createProductCharacteristicDTO(1L, "char val 1"),
//                createProductCharacteristicDTO(2L, 1),
//                createProductCharacteristicDTO(3L, "char val 4")
//        );
//        List<Characteristic> availableCharacteristicList = List.of(
//                createCharacteristic(1L, "char 1", true, List.of("char val 1", "char val 2")),
//                createCharacteristic(2L, "char 2", true, List.of("char val 3", "char val 4")),
//                createCharacteristic(3L, "char 3", false)
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productCharacteristicValidator.validate(productCharacteristicDTOList, availableCharacteristicList));
//
//        assertThat(thrown.getMessage()).isEqualTo("characteristic with id=2 is not a numeric one");
//    }
//
//    @Test
//    public void validate_verifyExceptionThrown_notValidTextValue() {
//        List<ProductCharacteristicDTO> productCharacteristicDTOList = List.of(
//                createProductCharacteristicDTO(1L, "char val 1"),
//                createProductCharacteristicDTO(2L, "char val 5"),
//                createProductCharacteristicDTO(3L, 1)
//        );
//        List<Characteristic> availableCharacteristicList = List.of(
//                createCharacteristic(1L, "char 1", true, List.of("char val 1", "char val 2")),
//                createCharacteristic(2L, "char 2", true, List.of("char val 3", "char val 4")),
//                createCharacteristic(3L, "char 3", false)
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productCharacteristicValidator.validate(productCharacteristicDTOList, availableCharacteristicList));
//
//        assertThat(thrown.getMessage()).isEqualTo("not valid text value for characteristic with id=2");
//    }
//}
