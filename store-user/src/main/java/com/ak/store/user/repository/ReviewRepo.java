//package com.ak.store.user.repository;
//
//import com.ak.store.user.model.entity.Review;
//import com.ak.store.user.model.projection.ReviewWithCommentCountProjection;
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//public interface ReviewRepo extends JpaRepository<Review, Long> {
//    @EntityGraph(attributePaths = {"user", "comments"})
//    List<Review> findAllWithConsumerAndCommentsByProductId(Long productId);
//
//    @Query("""
//            SELECT new com.ak.store.user.model.projection.ReviewWithCommentCountProjection(
//            r, r.user, SIZE(r.comments))
//            FROM Review r
//            JOIN r.user
//            WHERE r.productId = :productId
//            """)
//    List<ReviewWithCommentCountProjection> findAllWithConsumerAndCommentCountByProductId(Long productId);
//
//    @Query("SELECT r, r.comments FROM Review r JOIN r.comments WHERE r.productId = :productId AND r.user.id = :consumerId")
//    Optional<Review> findOneWithCommentsByProductIdAndConsumerId(Long productId, String consumerId);
//
//    @Query("SELECT r FROM Review r WHERE r.productId = :productId AND r.user.id = :consumerId")
//    Optional<Review> findOneByProductIdAndConsumerId(Long productId, UUID consumerId);
//
//    void deleteAllByProductId(Long productId);
//}
