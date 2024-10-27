package com.ak.store.user.service;

import com.ak.store.common.entity.user.User;
import com.ak.store.common.ResponseObject.UserPageResponse;
import com.ak.store.user.jdbc.UserDao;
import com.ak.store.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.ParsingUtils;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDao userDao) {
        this.userRepository = userRepository;
        this.userDao = userDao;
    }


    @Override
    public UserPageResponse findAll(int offset, int limit, String sortField, String direction) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortField);

        if("ASC".equalsIgnoreCase(direction)) {
            sort = Sort.by(Sort.Direction.ASC, sortField);
        }

        return convertToUserResponse(userRepository.findAll(
                PageRequest.of(offset, limit, sort)));
    }

    @Override
    public UserPageResponse findAllByName(String name) {
        return convertToUserResponse(userRepository.findAllByNameContainingIgnoreCase(
                name, PageRequest.of(0, 3)));
    }


    private UserPageResponse convertToUserResponse(Page<User> data) {
        return new UserPageResponse(data.getContent(), data.isLast(),
                data.getTotalPages(), data.getTotalElements());
    }
}