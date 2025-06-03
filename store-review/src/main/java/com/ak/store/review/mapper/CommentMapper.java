package com.ak.store.review.mapper;

import com.ak.store.review.model.document.Comment;
import com.ak.store.review.model.dto.CommentDTO;
import com.ak.store.review.model.dto.write.CommentWriteDTO;
import com.ak.store.review.model.form.CommentForm;
import com.ak.store.review.model.view.CommentView;
import org.bson.types.ObjectId;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CommentMapper {
    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    CommentDTO toCommentDTO(Comment c);
    List<CommentDTO> toCommentDTO(List<Comment> c);

    CommentView toCommentView(CommentDTO c);
    List<CommentView> toCommentView(List<CommentDTO> c);

    CommentWriteDTO toCommentWriteDTO(CommentForm cf);

    Comment toComment(CommentWriteDTO cw);

    @Named("objectIdToString")
    static String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }
}