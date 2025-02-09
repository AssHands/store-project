//package com.ak.store.catalogue.validator;
//
//import com.ak.store.catalogue.model.entity.ProductImage;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import static com.ak.store.catalogue.TestProductFactory.createProductImage;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertThrows;
//import static org.mockito.Mockito.mock;
//
//public class ProductImageValidatorTest {
//    private ProductImageValidator productImageValidator = new ProductImageValidator();
//
//    @Test
//    public void validate_verifyNoExceptionThrown() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[1]", "0",
//                "image[3]", "2",
//                "image[4]", "1"
//        );
//        List<MultipartFile> addImages = List.of(
//                mock(MultipartFile.class),
//                mock(MultipartFile.class)
//        );
//        List<String> deleteImageIndexes = List.of(
//                "0",
//                "2"
//        );
//        List<ProductImage> productImages = List.of(
//                createProductImage(0, "image 0"),
//                createProductImage(1, "image 1"),
//                createProductImage(2, "image 2")
//        );
//
//        productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages);
//    }
//    @Test
//    public void validate_verifyExceptionThrown_indexesMoreThanNine() {
//        Map<String, String> allImageIndexes = Collections.emptyMap();
//        List<MultipartFile> addImages = List.of(
//                mock(MultipartFile.class),
//                mock(MultipartFile.class),
//                mock(MultipartFile.class),
//                mock(MultipartFile.class),
//                mock(MultipartFile.class),
//                mock(MultipartFile.class),
//                mock(MultipartFile.class),
//                mock(MultipartFile.class)
//                );
//        List<String> deleteImageIndexes = List.of("1");
//        List<ProductImage> productImages = List.of(
//                createProductImage(0, "image 0"),
//                createProductImage(1, "image 1"),
//                createProductImage(2, "image 2")
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("индекс больше 9");
//    }
//
//    @Test
//    public void validateKeysAndValues_verifyExceptionThrown_unknownField() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[0]", "2",
//                "image[1]", "1",
//                "unknownField", "3",
//                "image[3]", "0"
//        );
//        List<MultipartFile> addImages = Collections.emptyList();
//        List<String> deleteImageIndexes = Collections.emptyList();
//        List<ProductImage> productImages = Collections.emptyList();
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("неизвестное поле");
//    }
//
//    @Test
//    public void validateKeysAndValues_verifyExceptionThrown_incorrectValue() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[0]", "2",
//                "image[1]", "1",
//                "image[2]", "incorrectValue",
//                "image[3]", "0"
//        );
//        List<MultipartFile> addImages = Collections.emptyList();
//        List<String> deleteImageIndexes = Collections.emptyList();
//        List<ProductImage> productImages = Collections.emptyList();
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("значение некорректно");
//    }
//
//    @Test
//    public void validateDeleteImageIndexes_verifyExceptionThrown_remoteIndexSpecified() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[0]", "2",
//                "image[1]", "1",
//                "image[2]", "0"
//        );
//        List<MultipartFile> addImages = Collections.emptyList();
//        List<String> deleteImageIndexes = List.of("1");
//        List<ProductImage> productImages = List.of(
//                createProductImage(0, "image 0"),
//                createProductImage(1, "image 1"),
//                createProductImage(2, "image 2")
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("указан удалённый индекс в old positions");
//    }
//
//    @Test
//    public void validateDeleteImageIndexes_verifyExceptionThrown_indexMissing() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[0]", "2",
//                "image[1]", "1",
//                "image[2]", "0"
//        );
//        List<MultipartFile> addImages = Collections.emptyList();
//        List<String> deleteImageIndexes = List.of("3");
//        List<ProductImage> productImages = List.of(
//                createProductImage(0, "image 0"),
//                createProductImage(1, "image 1"),
//                createProductImage(2, "image 2")
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("удаляемый индекс отсутствует");
//    }
//    @Test
//    public void validateOldImageIndexes_verifyExceptionThrown_incorrectIndex() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[1]", "1",
//                "image[2]", "2",
//                "image[5]", "4",
//                "image[3]", "0"
//        );
//        List<MultipartFile> addImages = List.of(
//                mock(MultipartFile.class),
//                mock(MultipartFile.class)
//        );
//        List<String> deleteImageIndexes = List.of("0");
//        List<ProductImage> productImages = List.of(
//                createProductImage(0, "image 0"),
//                createProductImage(1, "image 1"),
//                createProductImage(2, "image 2")
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("указан некорректный индекс");
//    }
//
//    @Test
//    public void validateNewImageIndexes_verifyExceptionThrown_incorrectAmountIndexes() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[0]", "0",
//                "image[1]", "1"
//        );
//        List<MultipartFile> addImages = Collections.emptyList();
//        List<String> deleteImageIndexes = Collections.emptyList();
//        List<ProductImage> productImages = List.of(
//                createProductImage(0, "image 0"),
//                createProductImage(1, "image 1"),
//                createProductImage(2, "image 2")
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("неверное кол-во индексов");
//    }
//
//    @Test
//    public void validateNewImageIndexes_verifyExceptionThrown_zeroIndexNotSpecified() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[0]", "1",
//                "image[1]", "2",
//                "image[2]", "3"
//        );
//        List<MultipartFile> addImages = Collections.emptyList();
//        List<String> deleteImageIndexes = Collections.emptyList();
//        List<ProductImage> productImages = List.of(
//                createProductImage(0, "image 0"),
//                createProductImage(1, "image 1"),
//                createProductImage(2, "image 2")
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("нет нулевого индекса");
//    }
//
//    @Test
//    public void validateNewImageIndexes_verifyExceptionThrown_IndexesAreNotInSequence() {
//        Map<String, String> allImageIndexes = Map.of(
//                "image[0]", "0",
//                "image[1]", "1",
//                "image[2]", "3",
//                "image[3]", "4"
//        );
//        List<MultipartFile> addImages = Collections.emptyList();
//        List<String> deleteImageIndexes = Collections.emptyList();
//        List<ProductImage> productImages = List.of(
//                createProductImage(0, "image 0"),
//                createProductImage(1, "image 1"),
//                createProductImage(2, "image 2"),
//                createProductImage(3, "image 3")
//        );
//
//        Exception thrown = assertThrows(
//                RuntimeException.class,
//                () -> productImageValidator.validate(allImageIndexes, addImages, deleteImageIndexes, productImages));
//
//        assertThat(thrown.getMessage()).isEqualTo("индексы идут не поочередно");
//    }
//}
