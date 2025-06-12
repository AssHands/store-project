package com.ak.store.common.snapshot.catalogue;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategorySnapshotPayload {
    @Builder.Default
    private CategorySnapshot category = new CategorySnapshot();

    @Builder.Default
    private List<Long> characteristics = new ArrayList<>();

    @Builder.Default
    private List<Long> relatedCategories = new ArrayList<>();
}