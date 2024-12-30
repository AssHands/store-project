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
    @Field(type = FieldType.Keyword, name = "characteristic_id")
    @JsonProperty("id")
    private Long id;

    @Field(type = FieldType.Keyword, name = "text_value")
    @JsonProperty("text_value")
    private String textValue;

    @Field(type = FieldType.Integer, name = "numeric_value")
    @JsonProperty("numeric_value")
    private Integer numericValue;
}
