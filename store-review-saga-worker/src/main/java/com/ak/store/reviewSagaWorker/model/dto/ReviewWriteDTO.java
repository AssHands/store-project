package com.ak.store.reviewSagaWorker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewWriteDTO {
    private String text;

    private String advantages;

    private String disadvantages;

    private Integer grade;
}
