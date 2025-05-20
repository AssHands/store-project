package com.ak.store.warehouse.mapper;

import com.ak.store.warehouse.model.dto.AvailableInventoryDTO;
import com.ak.store.warehouse.model.dto.InventoryDTO;
import com.ak.store.warehouse.model.dto.ReserveInventoryDTO;
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
    InventoryDTO toInventoryDTO(Inventory i);
    List<InventoryDTO> toInventoryDTO(List<Inventory> i);

    List<InventoryView> toInventoryView(List<InventoryDTO> i);

    List<ReserveInventoryDTO> toReserveInventoryDTO(List<ReserveInventoryForm> ri);

    List<AvailableInventoryDTO> toAvailableInventoryDTO(List<AvailableInventoryForm> ai);
}
