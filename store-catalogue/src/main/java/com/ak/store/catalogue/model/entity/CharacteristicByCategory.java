package com.ak.store.catalogue.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacteristicByCategory {
    private Long characteristicId;
    private String name;
    private String textValue;
}