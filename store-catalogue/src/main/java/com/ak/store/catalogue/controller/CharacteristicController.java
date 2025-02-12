package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.CharacteristicServiceFacade;
import com.ak.store.common.model.catalogue.view.CharacteristicView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/characteristics")
public class CharacteristicController {
    private final CharacteristicServiceFacade characteristicServiceFacade;

    @GetMapping
    public List<CharacteristicView> getAllAvailableCharacteristicByCategory(@RequestParam Long categoryId) {
        return characteristicServiceFacade.findAllCharacteristicByCategoryId(categoryId);
    }
}
