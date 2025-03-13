package com.ak.store.common.model.catalogue.form;

import com.ak.store.common.validationGroup.Create;
import com.ak.store.common.validationGroup.Update;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CharacteristicForm {
    @NotBlank(groups = { Create.class, Update.class })
    private String name;

    @Null(groups = Update.class)
    @NotNull(groups = Create.class)
    private Boolean isText;
}
