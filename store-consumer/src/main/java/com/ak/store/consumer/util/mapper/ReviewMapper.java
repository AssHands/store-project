package com.ak.store.consumer.util.mapper;

import com.ak.store.common.model.consumer.form.ReviewForm;
import com.ak.store.common.model.consumer.view.CommentView;
import com.ak.store.common.model.consumer.view.ReviewView;
import com.ak.store.consumer.model.entity.Comment;
import com.ak.store.consumer.model.entity.Consumer;
import com.ak.store.consumer.model.entity.Review;
import com.ak.store.consumer.model.projection.ReviewWithCommentCountProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReviewMapper {
    @Mapping(target = "id", source = "review.id")
    @Mapping(target = "productId", source = "review.productId")
    @Mapping(target = "text", source = "review.text")
    @Mapping(target = "advantages", source = "review.advantages")
    @Mapping(target = "disadvantages", source = "review.disadvantages")
    @Mapping(target = "grade", source = "review.grade")
    ReviewView toReviewView(ReviewWithCommentCountProjection review);

    CommentView toCommentView(Comment comment);

    @Mapping(target = "consumer", expression = "java(consumer)")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    Review toReview(ReviewForm reviewForm, Consumer consumer, Long productId);
}
