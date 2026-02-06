package com.ak.store.review.mapper;

import com.ak.store.review.model.command.WriteCommentCommand;
import com.ak.store.review.model.document.Comment;
import com.ak.store.review.model.dto.CommentDTO;
import com.ak.store.review.model.form.CommentForm;
import com.ak.store.review.model.view.CommentView;
import org.bson.types.ObjectId;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CommentMapper {
    CommentDTO toDTO(Comment document);

    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    @Mapping(target = "reviewId", source = "reviewId", qualifiedByName = "objectIdToString")
    CommentView toView(CommentDTO dto);

    WriteCommentCommand toWriteCommand(ObjectId commentId, UUID userId, CommentForm form);

    Comment toDocument(WriteCommentCommand command);

    @Named("objectIdToString")
    static String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToObjectId")
    static ObjectId objectIdToString(String id) {
        return id != null ? new ObjectId(id) : null;
    }
}