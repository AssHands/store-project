package com.ak.store.catalogue.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;


@Data
@Document(indexName = "product")
public class ProductDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Float)
    private float price;

    @Field(type = FieldType.Keyword, name = "category_id")
    @JsonProperty("category_id")
    private Long categoryId;

    @Field(type = FieldType.Integer, name = "amount_sales")
    @JsonProperty("amount_sales")
    private int amountSales;

    @Field(type = FieldType.Integer, name = "amount_reviews")
    @JsonProperty("amount_reviews")
    private int amountReviews;

    @Field(type = FieldType.Float)
    private float grade;

    @Field(type = FieldType.Nested, includeInParent = true)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ProductCharacteristicDocument> characteristics;
}