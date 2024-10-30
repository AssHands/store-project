package com.ak.store.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ProductDto {
    private Long id;

    private String title;

    private String description;

    private float price;

    private int categoryId;

    private Map<String, String> properties;
}
