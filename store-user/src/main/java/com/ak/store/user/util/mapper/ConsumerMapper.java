package com.ak.store.consumer.util.mapper;

import com.ak.store.common.model.consumer.form.ConsumerForm;
import com.ak.store.common.model.consumer.view.ConsumerPoorView;
import com.ak.store.consumer.model.entity.Consumer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ConsumerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carts", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "verificationCode", ignore = true)
    Consumer toConsumer(ConsumerForm consumerForm);

    ConsumerPoorView toConsumerPoorView(Consumer consumer);
}
