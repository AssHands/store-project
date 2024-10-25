package com.ak.store.user.service;

import com.ak.store.common.entity.user.User;
import org.springframework.data.domain.Page;


import java.util.List;

public interface UserService {
    Page<User> findAll(int offset, int limit);
}
