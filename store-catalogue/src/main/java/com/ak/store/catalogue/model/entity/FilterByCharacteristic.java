package com.ak.store.catalogue.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterByCharacteristic {
    private Long id;
    private Integer fromValue;
    private Integer toValue;
    private String textValue;
    private Long characteristicId;
    private String name;
}