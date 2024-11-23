package com.ak.store.common.payload.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPayload {
    @Size(min = 5, max = 50)
    private String title;

    @Size(min = 5, max = 150)
    private String description;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    private Float price;

    private Integer categoryId;

    private Map<String, String> properties;
}