package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.ProductServiceFacade;
import com.ak.store.common.model.catalogue.view.ProductRichView;
import com.ak.store.common.model.catalogue.dto.ImageDTO;
import com.ak.store.common.payload.catalogue.ProductWritePayload;
import com.ak.store.common.payload.search.ProductSearchResponse;
import com.ak.store.common.payload.search.SearchAvailableFiltersRequest;
import com.ak.store.common.payload.search.SearchAvailableFiltersResponse;
import com.ak.store.common.payload.search.SearchProductRequest;
import com.ak.store.common.validationGroup.Create;
import com.ak.store.common.validationGroup.Update;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products")
public class ProductController {
    private final ProductServiceFacade productServiceFacade;

    @GetMapping("{id}")
    public ProductRichView getOneProduct(@PathVariable("id") Long id) {
        return productServiceFacade.findOneProduct(id);
    }

    @DeleteMapping("{id}")
    public void deleteOneProduct(@PathVariable("id") Long id) {
        productServiceFacade.deleteOneProduct(id);
    }

    @PostMapping
    public void createOneProduct(@RequestBody @Validated(Create.class) ProductWritePayload productPayload) {
        productServiceFacade.createOneProduct(productPayload);
    }
    @PostMapping("batch") //todo: make validation for list
    public void createAllProduct(@RequestBody List<ProductWritePayload> productPayloads) {

        for(ProductWritePayload payload : productPayloads) {
            Errors errors = new BeanPropertyBindingResult(payload, "productPayload");
            //validator.validate(payload.getProduct(), errors, ProductWriteDTO.Save.class);

            if(errors.hasErrors()){
                //return ResponseEntity.badRequest().body(errors.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList()));
            }
        }

        productServiceFacade.createAllProduct(productPayloads);
    }

    @PatchMapping("{id}")
    public void updateOneProduct(@RequestBody @Validated(Update.class) ProductWritePayload productPayload,
                                       @PathVariable("id") Long productId) {
        productServiceFacade.updateOneProduct(productPayload, productId);
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
    //todo: validate data in controller
    @PatchMapping(value = "{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateAllProductImage(@PathVariable("id") Long productId,
                                      @RequestParam Map<String, String> allImageIndexes,
                                      @RequestParam(value = "add_images", required = false) List<MultipartFile> addImages,
                                      @RequestParam(value = "delete_images", required = false) List<String> deleteImageIndexes) {
        ImageDTO imageDTO = new ImageDTO(productId, allImageIndexes, addImages, deleteImageIndexes);

        productServiceFacade.saveOrUpdateAllImage(imageDTO);
    }

    @PostMapping("search")
    public ProductSearchResponse searchAllProduct(@RequestBody @Valid SearchProductRequest searchProductRequest) {
        System.out.println(searchProductRequest);
        return productServiceFacade.findAllProductBySearch(searchProductRequest);
    }

    @PostMapping("search/filters")
    public SearchAvailableFiltersResponse searchAllAvailableFilter(@RequestBody @Valid SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
        System.out.println(searchAvailableFiltersRequest);
        return productServiceFacade.findAllAvailableFilter(searchAvailableFiltersRequest);
    }
}
