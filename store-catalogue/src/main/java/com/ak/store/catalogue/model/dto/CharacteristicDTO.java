package com.ak.store.catalogue.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicDTO {
    private Long id;
    private String name;
    private Boolean isText;
}
