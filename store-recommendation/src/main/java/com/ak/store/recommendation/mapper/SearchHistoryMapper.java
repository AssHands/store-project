package com.ak.store.recommendation.mapper;

import com.ak.store.kafka.storekafkastarter.model.event.search.SearchEvent;
import com.ak.store.recommendation.model.command.WriteHistoryCommand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SearchHistoryMapper {
    WriteHistoryCommand toWriteCommand(SearchEvent event);
}
