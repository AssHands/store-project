package com.ak.store.reviewSagaWorker.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteReviewCommand {
    private String id;

    private UUID userId;

    private Long productId;

    private String text;

    private String advantages;

    private String disadvantages;

    private Integer grade;

    private LocalDateTime time;
}
