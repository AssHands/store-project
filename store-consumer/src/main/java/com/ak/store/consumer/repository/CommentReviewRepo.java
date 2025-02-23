package com.ak.store.consumer.repository;

import com.ak.store.consumer.model.entity.CommentReview;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReviewRepo extends JpaRepository<CommentReview, Long> {
    @Query("""
            SELECT cr, cr.consumer
            FROM CommentReview cr
            JOIN cr.consumer
            WHERE cr.review.id = :id
            """)
    List<CommentReview> findAllByReviewId(Long id);
}
