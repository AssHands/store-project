package com.ak.store.catalogue.model.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCharacteristicDocument {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("text_value")
    private String textValue;

    @JsonProperty("numeric_value")
    private Integer numericValue;
}
