package com.ak.store.reviewSagaWorker.mapper;

import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewSnapshot;
import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewUpdatedSnapshot;
import com.ak.store.reviewSagaWorker.model.command.WriteReviewCommand;
import com.ak.store.reviewSagaWorker.model.document.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReviewMapper {
    WriteReviewCommand toWriteCommand(ReviewSnapshot snapshot);

    void updateEntity(WriteReviewCommand command, @MappingTarget Review document);
}
