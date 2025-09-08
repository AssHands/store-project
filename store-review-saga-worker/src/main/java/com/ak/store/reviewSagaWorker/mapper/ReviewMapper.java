package com.ak.store.reviewSagaWorker.mapper;

import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewSnapshot;
import com.ak.store.reviewSagaWorker.model.dto.ReviewWriteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReviewMapper {
    ReviewWriteDTO toReviewWriteDTO(ReviewSnapshot rs);
}