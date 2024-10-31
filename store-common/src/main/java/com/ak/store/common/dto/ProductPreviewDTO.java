package com.ak.store.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPreviewDTO extends ProductDTOk implements ProductDTO {
    private Long id;

    private String title;

    private float price;

    private int categoryId;
}
