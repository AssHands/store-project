package com.ak.store.user.model.dto.write;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserWriteDTO {
    private String name;

    //todo это поле должно быть null при обновлении. при создании наоборот, не должно быть null
    private String email;

    private String password;
}
