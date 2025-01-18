package com.ak.store.catalogue.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {
    private Long id;

    private String title;

    private String description;

    @JsonProperty("current_price")
    private int currentPrice;

    @JsonProperty("discount_percentage")
    private int discountPercentage;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("amount_sales")
    private int amountSales;

    @JsonProperty("amount_reviews")
    private int amountReviews;

    private float grade;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ProductCharacteristicDocument> characteristics;
}