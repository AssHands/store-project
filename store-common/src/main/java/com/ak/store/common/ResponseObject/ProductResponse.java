package com.ak.store.common.ResponseObject;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minidev.json.JSONObject;

import java.util.Map;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;

    private String title;

    private String description;

    private float price;

    private int categoryId;

    private Map<String, String> properties;
}
