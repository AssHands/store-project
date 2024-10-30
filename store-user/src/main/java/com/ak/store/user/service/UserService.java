package com.ak.store.user.service;

import com.ak.store.common.ResponseObject.UserPageResponse;

public interface UserService {
    UserPageResponse findAll(int offset, int limit, String sortField, String direction);
    UserPageResponse findAllByName(String name);
}
