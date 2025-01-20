package com.ak.store.common.dto.catalogue.product;

import com.ak.store.common.validationGroup.Save;
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
public class ProductWriteDTO {
    @Size(min = 5, max = 50, groups = {Save.class, Update.class})
    @NotBlank(groups = Save.class)
    private String title;

    @Size(min = 5, max = 150, groups = {Save.class, Update.class})
    @NotBlank(groups = Save.class)
    private String description;

    @Min(value = 1, groups = {Save.class, Update.class})
    @Max(value = 10_000_000, groups = {Save.class, Update.class})
    @NotNull(groups = Save.class)
    private Integer fullPrice;

    @Min(value = 0, groups = {Save.class, Update.class})
    @Max(value = 99, groups = {Save.class, Update.class})
    private Integer discountPercentage;

    @NotNull(groups = Save.class)
    private Long categoryId;
}