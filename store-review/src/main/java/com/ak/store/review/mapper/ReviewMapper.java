package com.ak.store.review.mapper;

import com.ak.store.review.model.document.Review;
import com.ak.store.review.model.dto.ReviewDTO;
import com.ak.store.review.model.dto.write.ReviewWriteDTO;
import com.ak.store.review.model.form.ReviewForm;
import com.ak.store.review.model.view.ReviewView;
import org.bson.types.ObjectId;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReviewMapper {
    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    ReviewDTO toReviewDTO(Review r);
    List<ReviewDTO> toReviewDTO(List<Review> r);

    ReviewView toReviewView(ReviewDTO r);
    List<ReviewView> toReviewView(List<ReviewDTO> r);

    ReviewWriteDTO toReviewWriteDTO(ReviewForm rf);
    List<ReviewWriteDTO> toReviewWriteDTO(List<ReviewForm> rf);

    Review toReview(ReviewWriteDTO request);

    @Named("objectIdToString")
    static String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }
}