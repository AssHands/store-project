package com.ak.store.catalogue.utils;

import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.ProductImage;
import com.ak.store.catalogue.model.entity.TextValue;
import com.ak.store.catalogue.repository.CharacteristicRepo;
import com.ak.store.common.dto.catalogue.product.ProductCharacteristicDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class CatalogueValidator {

    private final CharacteristicRepo characteristicRepo;

    public CatalogueValidator(CharacteristicRepo characteristicRepo) {
        this.characteristicRepo = characteristicRepo;
    }

    public boolean validateCharacteristic(Characteristic availableCharacteristic,
                                          ProductCharacteristicDTO characteristicDTO) {
        if(characteristicDTO.getTextValue() != null) {
            if(characteristicDTO.getNumericValue() != null) {
                throw new RuntimeException("Characteristic with id=%s has both text value and numeric value"
                        .formatted(characteristicDTO.getId()));
            }

            if(!availableCharacteristic.getIsText()) {
                throw new RuntimeException("Characteristic with id=%s is not a text one"
                        .formatted(characteristicDTO.getId()));
            }

            boolean isContain = false;
            for(var textValue : availableCharacteristic.getTextValues()) {
                if(textValue.getTextValue().equals(characteristicDTO.getTextValue())) {
                    isContain = true;
                    break;
                }
            }

            if(!isContain) {
                throw new RuntimeException("Not valid text value for characteristic with id=%s"
                        .formatted(characteristicDTO.getId()));
            }
        }

        if(characteristicDTO.getNumericValue() != null) {
            if(availableCharacteristic.getIsText()) {
                throw new RuntimeException("Characteristic with id=%s is not a numeric one"
                        .formatted(characteristicDTO.getId()));
            }
        }

        return true;
    }

    public void validateCharacteristics(Iterable<ProductCharacteristicDTO> characteristics, Long categoryId) {
        var availableMap = characteristicRepo.findTextValuesByCategoryId(categoryId).stream()
                .collect(Collectors.toMap(
                        Characteristic::getId,
                        characteristic -> characteristic.getTextValues().stream().map(TextValue::getTextValue).toList()));

        for(var characteristic : characteristics) {
            List<String> textValues = availableMap.get(characteristic.getId());

            if(textValues == null) {
                throw new RuntimeException("Characteristic with id=%s is not available"
                        .formatted(characteristic.getId()));
            }

            if(characteristic.getTextValue() != null) {
                if(characteristic.getNumericValue() != null) {
                    throw new RuntimeException("Characteristic with id=%s has both text value and numeric value"
                            .formatted(characteristic.getId()));
                }

                if(textValues.isEmpty()) {
                    throw new RuntimeException("Characteristic with id=%s is not a text one"
                            .formatted(characteristic.getId()));
                }

                if(!textValues.contains(characteristic.getTextValue())) {
                    throw new RuntimeException("Not valid text value for characteristic with id=%s"
                            .formatted(characteristic.getId()));
                }
            }

            if(characteristic.getNumericValue() != null) {
                if(!textValues.isEmpty()) {
                    throw new RuntimeException("Characteristic with id=%s is not a numeric one"
                            .formatted(characteristic.getId()));
                }
            }
        }
    }

    public void validatePhotosFirst(Map<String, String> allImagePositions, List<MultipartFile> addImages,
                               List<String> deleteImageIndexes) {
        boolean isIncorrectFields = allImagePositions.keySet().stream()
                .filter(k -> !Pattern.compile("image\\[\\d]").matcher(k).matches())
                .anyMatch(k -> !Pattern.compile("delete_images").matcher(k).matches());

        if(isIncorrectFields)
            throw new RuntimeException("Неизвестное поле");

        List<Integer> newPosition = new ArrayList<>(allImagePositions.entrySet().stream()
                .filter(e -> Pattern.compile("image\\[\\d]").matcher(e.getKey()).matches())
                .map(e -> Integer.parseInt(e.getValue()))
                .toList());

        var isNewPositionDistinct = newPosition.size() == newPosition.stream().distinct().count();

        List<Integer> oldPosition = allImagePositions.keySet().stream()
                .filter(k -> Pattern.compile("image\\[\\d]").matcher(k).matches())
                .map(k -> Integer.parseInt(k.replaceAll("\\D", "")))
                .toList();

        var isOldPositionDistinct = oldPosition.size() == oldPosition.stream().distinct().count();

        if(!isNewPositionDistinct || !isOldPositionDistinct)
            throw new RuntimeException("Индексы не уникальны");

        int minimumSize = 0;

        if(addImages != null && deleteImageIndexes != null) {
            minimumSize = addImages.size() - deleteImageIndexes.size();
        }

        if(newPosition.size() < minimumSize)
            throw new RuntimeException("Кол-во индексов меньше ожидаемого");

        newPosition.sort(Integer::compare);

        if(newPosition.get(newPosition.size() - 1) > 9)
            throw new RuntimeException("Индекс больше 9");

        for (int i = 0; i < newPosition.size() - 1; i++) {
            if(i == 0 && newPosition.get(i) != 0)
                throw new RuntimeException("Нет нулевого индекса");

            if(newPosition.get(i) + 1 != newPosition.get(i + 1))
                throw new RuntimeException("Индексы идут не поочередно");
        }
    }

    public void validateImages(Map<String, String> allImagePositions, List<MultipartFile> addImages,
                                     List<String> deleteImageIndexes, List<ProductImage> productImages) {
        int size = productImages.size();
        if(addImages != null)
            size += addImages.size();
        if(deleteImageIndexes != null)
            size -= deleteImageIndexes.size();

        if(size > 9)
            throw new RuntimeException("Индекс больше 9");

        boolean isIncorrectKeys = allImagePositions.keySet().stream()
                .filter(k -> !Pattern.compile("image\\[\\d]").matcher(k).matches())
                .anyMatch(k -> !Pattern.compile("delete_images").matcher(k).matches());

        if(isIncorrectKeys)
            throw new RuntimeException("Неизвестное поле");

        boolean isIncorrectValues = allImagePositions.values().stream()
                .anyMatch(v -> !Pattern.compile("\\d").matcher(v).matches());

        if(isIncorrectValues)
            throw new RuntimeException("значение некорректно");

        List<Integer> oldPositions = allImagePositions.keySet().stream()
                .filter(k -> Pattern.compile("image\\[\\d]").matcher(k).matches())
                .map(k -> k.replaceAll("\\D", ""))
                .map(Integer::parseInt)
                .toList();

        if(deleteImageIndexes != null) {
            boolean isSpecifyDeletedIndex = oldPositions.stream()
                    .anyMatch(index -> deleteImageIndexes.contains(index.toString()));

            if(isSpecifyDeletedIndex)
                throw new RuntimeException("Указан удалённый индекс");

            int countExistingDeletedIndexes = (int) productImages.stream()
                    .filter(image -> deleteImageIndexes.contains(image.getIndex().toString()))
                    .count();

            if(countExistingDeletedIndexes != deleteImageIndexes.size())
                throw new RuntimeException("Удаляемый индекс отсутствует");
        }

        List<Integer> existingIndexes = new ArrayList<>();

        if(deleteImageIndexes != null) {
            existingIndexes.addAll(productImages.stream()
                    .map(ProductImage::getIndex)
                    .filter(index -> !deleteImageIndexes.contains(index.toString()))
                    .sorted()
                    .toList());
        }

        if(addImages != null) {
            int lastIndex = existingIndexes.isEmpty() ? -1 : existingIndexes.get(existingIndexes.size() - 1);
            for(var newImage : addImages)
                existingIndexes.add(++lastIndex);
        }

        if(!existingIndexes.containsAll(oldPositions)) {
            throw new RuntimeException("Указан некорректный индекс");
        }

        List<Integer> newPositions = allImagePositions.entrySet().stream()
                .filter(e -> Pattern.compile("image\\[\\d]").matcher(e.getKey()).matches())
                .map(e -> Integer.parseInt(e.getValue()))
                .sorted()
                .toList();

        if(size != newPositions.size())
            throw new RuntimeException("Неверное кол-во индексов");

        if(size != 0 && newPositions.get(0) != 0)
            throw new RuntimeException("Нет нулевого индекса");

        for (int i = 0; i < newPositions.size() - 1; i++) {
            if(newPositions.get(i) + 1 != newPositions.get(i + 1))
                throw new RuntimeException("Индексы идут не поочередно");
        }
    }
}