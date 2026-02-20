package com.ak.store.catalogue.controller;

import com.ak.store.catalogue.facade.ProductCharacteristicFacade;
import com.ak.store.catalogue.mapper.ProductCharacteristicMapper;
import com.ak.store.catalogue.model.command.WriteProductCharacteristicPayloadCommand;
import com.ak.store.catalogue.model.form.WriteProductCharacteristicPayloadForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCharacteristicControllerTest {

    @Mock
    private ProductCharacteristicFacade facade;
    @Mock
    private ProductCharacteristicMapper mapper;

    @InjectMocks
    private ProductCharacteristicController controller;

    @Test
    void updateAllShouldOverrideProductIdFromPath() {
        var form = WriteProductCharacteristicPayloadForm.builder()
                .productId(999L)
                .build();
        var command = WriteProductCharacteristicPayloadCommand.builder().productId(123L).build();

        when(mapper.toWritePayloadCommand(any())).thenReturn(command);

        controller.updateAll(123L, form);

        verify(mapper).toWritePayloadCommand(form);
        verify(facade).updateAll(command);
    }
}
