package com.ak.store.common.dto.catalogue;

import com.ak.store.common.dto.search.Filters;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWriteDTO {
    private String title;

    private String description;

    private Float price;

    @JsonPropertyOrder("category_id")
    private Long categoryId;

    private Filters filters;
}
