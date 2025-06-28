package com.ak.store.payment.mapper;

import com.ak.store.payment.model.dto.UserBalanceDTO;
import com.ak.store.payment.model.entity.UserBalance;
import com.ak.store.payment.model.view.UserBalanceView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserBalanceMapper {
    UserBalanceDTO toUserBalanceDTO(UserBalance ub);

    UserBalanceView toUserBalanceView(UserBalanceDTO ub);
}
