package com.ak.store.review.mapper;

import com.ak.store.review.model.document.Reaction;
import com.ak.store.review.model.dto.ReactionDTO;
import com.ak.store.review.model.view.ReactionView;
import org.bson.types.ObjectId;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReactionMapper {
    ReactionDTO toReactionDTO(Reaction r);
    List<ReactionDTO> toReactionDTO(List<Reaction> r);

    @Mapping(target = "reviewId", source = "reviewId", qualifiedByName = "objectIdToString")
    ReactionView toReactionView(ReactionDTO r);
    List<ReactionView> toReactionView(List<ReactionDTO> r);

    @Named("objectIdToString")
    static String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToObjectId")
    static ObjectId objectIdToString(String id) {
        return id != null ? new ObjectId(id) : null;
    }
}