package com.ak.store.reviewSagaWorker.mapper;

import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewSnapshot;
import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewUpdatedSnapshot;
import com.ak.store.reviewSagaWorker.model.command.WriteReviewCommand;
import com.ak.store.reviewSagaWorker.model.document.Review;
import org.bson.types.ObjectId;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReviewMapper {
    WriteReviewCommand toWriteCommand(ReviewSnapshot snapshot);

    @Mapping(target = "id", source = "id", qualifiedByName = "stringToObjectId")
    void updateEntity(WriteReviewCommand command, @MappingTarget Review document);

    @Named("objectIdToString")
    static String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToObjectId")
    static ObjectId objectIdToString(String id) {
        return id != null ? new ObjectId(id) : null;
    }
}
