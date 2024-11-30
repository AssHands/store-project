package com.ak.store.common.dto.catalogue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;

    @JsonProperty("parent_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentId;

    @NotBlank
    private String name;

    @JsonProperty("child_categories")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryDTO> childCategories = new ArrayList<>();
}