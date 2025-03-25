package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.ProductFacade;
import com.ak.store.common.model.catalogue.view.ProductPoorView;
import com.ak.store.common.model.catalogue.dto.ProductPriceDTO;
import com.ak.store.common.model.catalogue.view.ProductRichView;
import com.ak.store.common.model.catalogue.form.ImageForm;
import com.ak.store.common.payload.catalogue.ProductWritePayload;
import com.ak.store.common.validationGroup.Create;
import com.ak.store.common.validationGroup.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products")
public class ProductController {
    private final ProductFacade productFacade;

    @GetMapping("{id}/rich")
    public ProductRichView getOneRich(@PathVariable Long id) {
        return productFacade.findOneRich(id);
    }

    @GetMapping("{id}/poor")
    public ProductPoorView getOnePoor(@PathVariable Long id) {
        return productFacade.findOnePoor(id);
    }

    @PostMapping("poor")
    public List<ProductPoorView> getAllPoor(@RequestBody List<Long> ids) {
        return productFacade.findAllPoor(ids);
    }

    @PostMapping("price")
    public List<ProductPriceDTO> getAllPrice(@RequestBody List<Long> ids) {
        return productFacade.getAllPrice(ids);
    }

    @GetMapping("exist/{id}")
    public Boolean existOne(@PathVariable Long id) {
        return productFacade.existOne(id);
    }

    @PostMapping("available")
    public Boolean availableAll(@RequestBody List<Long> ids) {
        return productFacade.availableAll(ids);
    }

    @GetMapping("available/{id}")
    public Boolean availableOne(@PathVariable Long id) {
        return productFacade.availableOne(id);
    }

    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable Long id) {
        productFacade.deleteOne(id);
    }

    @PostMapping
    public Long createOne(@RequestBody @Validated(Create.class) ProductWritePayload productPayload) {
        return productFacade.createOne(productPayload);
    }

    @PostMapping("batch")
    public Long createAll(@RequestBody List<ProductWritePayload> productPayloads) {
        return productFacade.createAll(productPayloads);
    }

    @PatchMapping("{id}")
    public Long updateOne(@RequestBody @Validated(Update.class) ProductWritePayload productPayload,
                          @PathVariable("id") Long productId) {
        return productFacade.updateOne(productPayload, productId);
    }

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
    //todo: validate data in controller
    @PatchMapping(value = "{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long updateAllProductImage(@PathVariable("id") Long productId,
                                      @RequestParam Map<String, String> allImageIndexes,
                                      @RequestParam(value = "add_images", required = false) List<MultipartFile> addImages,
                                      @RequestParam(value = "delete_images", required = false) List<String> deleteImageIndexes) {
        ImageForm imageForm = new ImageForm(productId, allImageIndexes, addImages, deleteImageIndexes);

        return productFacade.saveOrUpdateAllImage(imageForm);
    }

//    @PostMapping("search")
//    public ProductSearchResponse searchAllProduct(@AuthenticationPrincipal Jwt accessToken,
//                                                  @RequestBody @Valid SearchProductRequest searchProductRequest) {
//        return productServiceFacade.findAllBySearch(accessToken.getSubject(), searchProductRequest);
//    }
//
//    @PostMapping("search/filters")
//    public SearchAvailableFiltersResponse searchAllAvailableFilters(@RequestBody @Valid SearchAvailableFiltersRequest searchAvailableFiltersRequest) {
//        return productServiceFacade.findAllAvailableFilter(searchAvailableFiltersRequest);
//    }
}
