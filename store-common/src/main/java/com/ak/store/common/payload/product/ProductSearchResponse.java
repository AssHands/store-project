package com.ak.store.common.payload.product;

import com.ak.store.common.dto.catalogue.ProductReadDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchResponse {
    @JsonProperty("search_after")
    private List<Object> searchAfter;

    private List<ProductReadDTO> content;
}