package com.ak.store.user.service;

import com.ak.store.common.entity.user.User;
import com.ak.store.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Page<User> findAll(int offset, int limit) {
        return userRepository.findAll(PageRequest.of(offset, limit));
    }
}
