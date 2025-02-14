package com.ak.store.common.model.consumer.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewView {
    private Long id;

    private ConsumerPoorView consumer;

    private List<CommentReviewView> comments = new ArrayList<>();

    private Long productId;

    private String text;

    private String advantages;

    private String disadvantages;

    private Integer grade;

    private Integer amountLikes;

    private Integer amountDislikes;
}
