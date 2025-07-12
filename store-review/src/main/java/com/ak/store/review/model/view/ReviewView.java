package com.ak.store.review.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewView {
    private String id;

    private UUID userId;

    private Long productId;

    private String text;

    private String advantages;

    private String disadvantages;

    private Integer commentAmount;

    private Integer grade;

    private Integer likeAmount;

    private Integer dislikeAmount;

    private LocalDateTime time;

    private String status;
}
