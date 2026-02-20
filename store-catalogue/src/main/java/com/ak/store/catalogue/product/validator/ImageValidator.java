package com.ak.store.catalogue.product.validator;

import com.ak.store.catalogue.model.dto.ImageDTO;
import com.ak.store.catalogue.model.command.WriteImageCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class ImageValidator {
    public void validate(WriteImageCommand request, List<ImageDTO> images) {
        int expectedSize = images.size();
        if(request.getAddImages() != null)
            expectedSize += request.getAddImages().size();
        if(request.getDeleteImageIndexes() != null)
            expectedSize -= request.getDeleteImageIndexes().size();

        if(expectedSize > 9)
            throw new RuntimeException("индекс больше 9");

        validateKeysAndValues(request.getAllImageIndexes());

        List<Integer> oldImageIndexes = request.getAllImageIndexes().keySet().stream()
                .filter(k -> Pattern.compile("image\\[\\d]").matcher(k).matches())
                .map(k -> k.replaceAll("\\D", ""))
                .map(Integer::parseInt)
                .toList();

        validateDeleteImageIndexes(request.getDeleteImageIndexes(), oldImageIndexes, images);
        validateOldImageIndexes(request.getDeleteImageIndexes(), request.getAddImages(), images, oldImageIndexes);
        validateNewImageIndexes(request.getAllImageIndexes(), expectedSize);
    }

    private void validateOldImageIndexes(List<String> deleteImageIndexes, List<MultipartFile> addImages,
                                         List<ImageDTO> images, List<Integer> oldImageIndexes) {
        List<Integer> existingImageIndexes = new ArrayList<>();
        int lastIndex = images.stream()
                .map(ImageDTO::getIndex)
                .max(Integer::compareTo)
                .orElse(-1);

        if(deleteImageIndexes != null) {
            existingImageIndexes.addAll(images.stream()
                    .map(ImageDTO::getIndex)
                    .filter(index -> !deleteImageIndexes.contains(index.toString()))
                    .sorted()
                    .toList());
        }

        if(addImages != null) {
            for(var newImage : addImages)
                existingImageIndexes.add(++lastIndex);
        }

        if(!existingImageIndexes.containsAll(oldImageIndexes)) {
            throw new RuntimeException("указан некорректный индекс");
        }
    }

    private void validateKeysAndValues(Map<String, String> Fields) {
        boolean isUnknownKey = Fields.keySet().stream()
                .filter(k -> !Pattern.compile("image\\[\\d]").matcher(k).matches())
                .anyMatch(k -> !Pattern.compile("delete_images").matcher(k).matches());

        if(isUnknownKey)
            throw new RuntimeException("неизвестное поле");

        boolean isIncorrectValue = Fields.values().stream()
                .anyMatch(v -> !Pattern.compile("\\d").matcher(v).matches());

        if(isIncorrectValue)
            throw new RuntimeException("значение некорректно");
    }

    private void validateNewImageIndexes(Map<String, String> allImageIndexes, int expectedSize) {
        List<Integer> newImageIndexes = allImageIndexes.entrySet().stream()
                .filter(e -> Pattern.compile("image\\[\\d]").matcher(e.getKey()).matches())
                .map(e -> Integer.parseInt(e.getValue()))
                .sorted()
                .toList();

        if(expectedSize != newImageIndexes.size())
            throw new RuntimeException("неверное кол-во индексов");

        if(expectedSize != 0 && newImageIndexes.get(0) != 0)
            throw new RuntimeException("нет нулевого индекса");

        for (int i = 0; i < newImageIndexes.size() - 1; i++) {
            if(newImageIndexes.get(i) + 1 != newImageIndexes.get(i + 1))
                throw new RuntimeException("индексы идут не поочередно");
        }
    }

    private void validateDeleteImageIndexes(List<String> deleteImageIndexes, List<Integer> oldImageIndexes,
                                            List<ImageDTO> images) {
        if(deleteImageIndexes != null) {
            boolean isSpecifyDeletedIndex = oldImageIndexes.stream()
                    .anyMatch(index -> deleteImageIndexes.contains(index.toString()));

            if(isSpecifyDeletedIndex)
                throw new RuntimeException("указан удалённый индекс в old positions");

            int countExistingDeletedIndexes = (int) images.stream()
                    .filter(image -> deleteImageIndexes.contains(image.getIndex().toString()))
                    .count();

            if(countExistingDeletedIndexes != deleteImageIndexes.size())
                throw new RuntimeException("удаляемый индекс отсутствует");
        }
    }
}
