package com.ak.store.consumer.repository;

import com.ak.store.consumer.model.entity.CommentReview;
import com.ak.store.consumer.model.entity.Review;
import com.ak.store.consumer.model.projection.ReviewWithCommentCountProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"consumer", "comments"})
    List<Review> findAllWithConsumerAndCommentsByProductId(Long productId);

    @Query("""
            SELECT new com.ak.store.consumer.model.projection.ReviewWithCommentCountProjection(
            r, r.consumer, SIZE(r.comments))
            FROM Review r
            JOIN r.consumer
            WHERE r.productId = :productId
            """)
    List<ReviewWithCommentCountProjection> findAllWithConsumerAndCommentCountByProductId(Long productId);

    @Query("SELECT r, r.comments FROM Review r JOIN r.comments WHERE r.productId = :productId AND r.consumer.id = :consumerId")
    Optional<Review> findOneWithCommentsByProductIdAndConsumerId(Long productId, Long consumerId);

    @Query("SELECT r FROM Review r WHERE r.productId = :productId AND r.consumer.id = :consumerId")
    Optional<Review> findOneByProductIdAndConsumerId(Long productId, Long consumerId);

    void deleteAllByProductId(Long productId);
}
