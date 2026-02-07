package com.ak.store.catalogue.model.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteCategoryCommand {
    private Long id;
    private String name;
    private Long parentId;
}
