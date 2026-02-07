package com.ak.store.recommendation.mapper;

import com.ak.store.recommendation.model.dto.RecommendationDTO;
import com.ak.store.recommendation.model.view.RecommendationView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RecommendationMapper {
    RecommendationView toView(RecommendationDTO dto);
}
