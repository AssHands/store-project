package com.ak.store.consumer.repository;

import com.ak.store.consumer.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    @Query("""
            SELECT c, c.consumer
            FROM Comment c
            JOIN c.consumer
            WHERE c.review.id = :id
            """)
    List<Comment> findAllByReviewId(Long id);
}