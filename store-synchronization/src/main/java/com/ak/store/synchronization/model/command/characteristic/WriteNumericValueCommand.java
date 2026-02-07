package com.ak.store.synchronization.model.command.characteristic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteNumericValueCommand {
    private Integer fromValue;

    private Integer toValue;
}
