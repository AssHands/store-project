package com.ak.store.recommendation.model.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteHistoryCommand {
    private UUID userId;

    private Long categoryId;
}
