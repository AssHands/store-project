package com.ak.store.catalogue.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicByCategory {
    private Long id;
    private Long characteristicId;
    private String name;
    private String textValue;
}
