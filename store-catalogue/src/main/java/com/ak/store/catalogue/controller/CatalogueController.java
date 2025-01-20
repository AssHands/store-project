package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.service.CatalogueService;
import com.ak.store.common.dto.catalogue.product.*;
import com.ak.store.common.dto.search.Filters;
import com.ak.store.common.payload.product.ProductWritePayload;
import com.ak.store.common.validationGroup.Save;
import com.ak.store.common.validationGroup.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController
@RequestMapping("api/v1/catalogue")
public class CatalogueController {

    private final CatalogueService catalogueService;

    @Autowired
    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    @GetMapping("products/{id}")
    public ProductFullReadDTO getOneProductById(@PathVariable("id") Long id) {
        return catalogueService.findOneProductById(id);
    }

    @DeleteMapping("products/{id}")
    public void deleteOneProductById(@PathVariable("id") Long id) {
        catalogueService.deleteOneProduct(id);
    }

    @PostMapping("products")
    public ProductWriteDTO createOneProduct(@RequestBody @Validated(Save.class) ProductWritePayload productPayload) {
        catalogueService.createOneProduct(productPayload);
        return null;
    }

    @PostMapping("products/batch")
    //todo: make validation for list
    public ProductWriteDTO createAllProduct(@RequestBody List<ProductWritePayload> productPayloads) {

        for(ProductWritePayload payload : productPayloads) {
            Errors errors = new BeanPropertyBindingResult(payload, "productPayload");
            //validator.validate(payload.getProduct(), errors, ProductWriteDTO.Save.class);

            if(errors.hasErrors()){
                //return ResponseEntity.badRequest().body(errors.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()));
            }
        }

        catalogueService.createAllProduct(productPayloads);
        return null;
    }

    @PatchMapping("products/{id}")
    public ProductWriteDTO updateOneProduct(@RequestBody @Validated(Update.class) ProductWritePayload productPayload,
                                            @PathVariable("id") Long productId) {
        catalogueService.updateOneProduct(productPayload, productId);
        return null;
    }

    @GetMapping("categories")
    public List<CategoryDTO> getAllCategory(@RequestParam(required = false) String name) {
        if(name != null)
            return catalogueService.findAllCategoryByName(name);

        return catalogueService.findAllCategory();
    }

    @GetMapping("characteristics")
    public Filters getAllAvailableCharacteristic(@RequestParam Long categoryId) {
        return catalogueService.findAllAvailableCharacteristic(categoryId);
    }

    /**
     * Индекс является самой последовательностью изображений. <br>
     * <br>
     * Все новые изображения добавляються под следующем индексом последнего индекса. <br>
     * <br>
     * Удаление изображений происходит по указанному индексу. <br>
     * <br>
     * При обращении к endpoint'у требуется указывать индекс для каждого изображения,
     * индексы удалённых изображений указывать не надо. <br>
     * -------------------------------------------------------------- <br>
     * Например: есть пустой список изображений, к нему добавляем фотографии a, b, c. <br>
     * Им присваиваються индексы в порядке добавления: <br>
     * a[0] <br>
     * b[1] <br>
     * c[2] <br>
     * <br>
     * Обязательно надо указать их новую или текущую последовательность. <br>
     * image[0] -> 1 <br>
     * image[1] -> 2 <br>
     * image[2] -> 0 <br>
     * В таком случае их последователность будет равна: <br>
     * c[0] <br>
     * a[1] <br>
     * b[2] <br>
     *<br>
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
     * Также требуеться указать новую последовательность: <br>
     * image[0] -> 1 <br>
     * image[3] -> 0 <br>
     * image[4] -> 2 <br>
     * <br>
     * В итоге будет данная последовательность изображений: <br>
     * d[0] <br>
     * a[1] <br>
     * e[2] <br>
     */
    @PatchMapping(value = "products/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateAllProductImage(@PathVariable("id") Long productId,
                                      @RequestParam Map<String, String> allImagePositions,
                                      @RequestParam(value = "add_images", required = false) List<MultipartFile> addImages,
                                      @RequestParam(value = "delete_images", required = false) List<String> deleteImageIndexes) {
        catalogueService.saveOrUpdateAllImage(productId, allImagePositions, addImages, deleteImageIndexes);
    }

    @PatchMapping("test")
    public void test() { }
}