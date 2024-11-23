package com.ak.store.common.Response;

import com.ak.store.common.dto.product.ProductDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    @JsonProperty("search_after") //todo: doesnt work
    private List<Object> searchAfter;

    private List<ProductDTO> content;
}