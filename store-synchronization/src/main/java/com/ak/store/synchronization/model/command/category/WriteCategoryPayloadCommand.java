package com.ak.store.synchronization.model.command.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteCategoryPayloadCommand {
    private WriteCategoryCommand category;

    private List<Long> characteristics;
}
