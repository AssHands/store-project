package com.ak.store.catalogue.facade;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCharacteristicFacadeTest {

    @Test
    void updateAllShouldBeTransactional() throws NoSuchMethodException {
        Method method = ProductCharacteristicFacade.class
                .getDeclaredMethod("updateAll", com.ak.store.catalogue.model.command.WriteProductCharacteristicPayloadCommand.class);

        assertThat(method.isAnnotationPresent(Transactional.class)).isTrue();
    }
}
