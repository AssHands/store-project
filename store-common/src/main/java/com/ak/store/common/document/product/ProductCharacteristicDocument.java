package com.ak.store.common.document.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class ProductCharacteristicDocument {
    @Field(type = FieldType.Keyword, name = "characteristic_id")
    @JsonProperty("characteristic_id")
    private Long characteristicId;

    @Field(type = FieldType.Keyword, name = "text_value")
    @JsonProperty("text_value")
    private String textValue;

    @Field(type = FieldType.Integer, name = "numeric_value")
    @JsonProperty("numeric_value")
    private Integer numericValue;
}
