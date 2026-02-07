package com.ak.store.synchronization.model.command.characteristic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteCharacteristicPayloadCommand {
    private WriteCharacteristicCommand characteristic;
    private List<String> textValues;
    private List<WriteNumericValueCommand> numericValues;
}
