package com.ak.store.common.ResponseObject;

import com.ak.store.common.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPageResponse {
    private List<User> content;

    private boolean last;
    private int totalPages;
    private Long totalElements;
}
