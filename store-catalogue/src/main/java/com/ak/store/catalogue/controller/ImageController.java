package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.ImageFacade;
import com.ak.store.catalogue.model.command.WriteImageCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products/images")
public class ImageController {
    private final ImageFacade imageFacade;

    /**
     * Индекс является самой последовательностью изображений. <br>
     * <br>
     * Все новые изображения добавляются под следующем индексом последнего индекса. <br>
     * <br>
     * Удаление изображений происходит по указанному индексу. <br>
     * <br>
     * При обращении к endpoint'у требуется указывать индекс для каждого изображения,
     * индексы удалённых изображений указывать не надо. <br>
     * -------------------------------------------------------------- <br>
     * Например: есть пустой список изображений, к нему добавляем фотографии a, b, c. <br>
     * Им присваиваются индексы в порядке добавления: <br>
     * a[0] <br>
     * b[1] <br>
     * c[2] <br>
     * <br>
     * Обязательно надо указать их новую или текущую последовательность. <br>
     * image[0] -> 1 <br>
     * image[1] -> 2 <br>
     * image[2] -> 0 <br>
     * В таком случае их последовательность будет равна: <br>
     * c[0] <br>
     * a[1] <br>
     * b[2] <br>
     * <br>
     * При удалении изображения мы указываем индекс изображения: <br>
     * delete_images -> 1, 2 <br>
     * add_images -> d, e <br>
     * В таком случае текущая последовательность будет выглядеть так: <br>
     * a[0] <br>
     * null[1] <br>
     * null[2] <br>
     * d[3] <br>
     * e[4] <br>
     * <br>
     * Также требуется указать новую последовательность: <br>
     * image[0] -> 1 <br>
     * image[3] -> 0 <br>
     * image[4] -> 2 <br>
     * <br>
     * В итоге будет данная последовательность изображений: <br>
     * d[0] <br>
     * a[1] <br>
     * e[2] <br>
     */
    @PatchMapping(value = "images/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long updateAllImage(@PathVariable("id") Long productId,
                               @RequestParam Map<String, String> allImageIndexes,
                               @RequestParam(value = "add_images", required = false) List<MultipartFile> addImages,
                               @RequestParam(value = "delete_images", required = false) List<String> deleteImageIndexes) {
        var command = new WriteImageCommand(productId, allImageIndexes, addImages, deleteImageIndexes);
        return imageFacade.updateAllImage(command);
    }
}