package com.ak.store.warehouse.mapper;

import com.ak.store.warehouse.model.command.AvailableInventoryCommand;
import com.ak.store.warehouse.model.dto.InventoryDTO;
import com.ak.store.warehouse.model.command.ReserveInventoryCommand;
import com.ak.store.warehouse.model.entity.Inventory;
import com.ak.store.warehouse.model.form.AvailableInventoryForm;
import com.ak.store.warehouse.model.form.ReserveInventoryForm;
import com.ak.store.warehouse.model.view.InventoryView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface InventoryMapper {
    InventoryDTO toDTO(Inventory entity);

    InventoryView toView(InventoryDTO dto);

    ReserveInventoryCommand toReserveInventoryCommand(ReserveInventoryForm form);

    AvailableInventoryCommand toAvailableInventoryCommand(AvailableInventoryForm form);
}
