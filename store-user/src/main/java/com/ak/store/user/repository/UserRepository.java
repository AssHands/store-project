package com.ak.store.user.repository;

import com.ak.store.common.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneById(Long id);
    Optional<User> findOneByNameContaining(String name);
    Page<User> findAllByNameContainingIgnoreCase(String name, PageRequest pageable);
}