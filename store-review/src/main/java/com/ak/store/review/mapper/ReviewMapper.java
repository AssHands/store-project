package com.ak.store.review.mapper;

import com.ak.store.kafka.storekafkastarter.model.snapshot.review.ReviewSnapshot;
import com.ak.store.review.model.command.WriteReviewCommand;
import com.ak.store.review.model.document.Review;
import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.model.form.ReviewForm;
import com.ak.store.review.model.view.ReviewView;
import org.bson.types.ObjectId;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReviewMapper {
    Review toDocument(WriteReviewCommand command);

    ReviewDTO toDTO(Review document);

    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    ReviewSnapshot toSnapshot(Review document);

    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    ReviewSnapshot toSnapshot(ReviewDTO dto);

    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    ReviewView toView(ReviewDTO dto);

    WriteReviewCommand toWriteCommand(UUID userId, ReviewForm form);

    @Named("objectIdToString")
    static String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToObjectId")
    static ObjectId stringToObjectId(String id) {
        return id != null ? new ObjectId(id) : null;
    }


    void updateDocument(WriteReviewCommand command, @MappingTarget Review document);
}