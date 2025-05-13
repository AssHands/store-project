package com.ak.store.catalogue.model.form;

import com.ak.store.common.validationGroup.Create;
import com.ak.store.common.validationGroup.Update;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductFormNew {
    @Size(min = 5, max = 70, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String title;

    @Size(min = 5, max = 150, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String description;

    @Min(value = 1, groups = {Create.class, Update.class})
    @Max(value = 10_000_000, groups = {Create.class, Update.class})
    @NotNull(groups = Create.class)
    private Integer fullPrice;

    @Min(value = 0, groups = {Create.class, Update.class})
    @Max(value = 99, groups = {Create.class, Update.class})
    private Integer discountPercentage;

    @NotNull(groups = Create.class)
    private Boolean isAvailable;

    @NotNull(groups = Create.class)
    private Long categoryId;
}