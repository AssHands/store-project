package com.ak.store.common.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPayload {
    private String title;

    private String description;

    private Float price;

    private Integer categoryId;

    private Map<String, String> properties;
}
