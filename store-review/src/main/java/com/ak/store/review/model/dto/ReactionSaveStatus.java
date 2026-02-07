package com.ak.store.review.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReactionSaveStatus {
    CREATED,
    UPDATED,
    UNCHANGED //ничего не произошло. лайк уже стоял и при вызове метода likeOne ничего не измеилось
}