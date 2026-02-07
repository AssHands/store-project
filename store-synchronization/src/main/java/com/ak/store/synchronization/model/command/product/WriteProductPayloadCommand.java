package com.ak.store.synchronization.model.command.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteProductPayloadCommand {
    private WriteProductCommand product;
    private List<WriteProductCharacteristicCommand> characteristics;
    private List<WriteImageCommand> images;
}
