package com.ak.store.reviewSagaWorker.mapper;

import com.ak.store.reviewSagaWorker.model.dto.ReviewRequest;
import com.ak.store.reviewSagaWorker.model.dto.write.ReviewWriteDTO;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReviewMapper {
    ReviewWriteDTO toReviewWriteDTO(ReviewRequest rr);
}