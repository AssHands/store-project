package com.ak.store.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private ObjectId id;

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

    //todo сделать так, чтобы mapper мапил enum в String
    private String status;
}