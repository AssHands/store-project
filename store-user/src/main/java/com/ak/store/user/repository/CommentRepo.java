//package com.ak.store.user.repository;
//
//import com.ak.store.user.model.entity.Comment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface CommentRepo extends JpaRepository<Comment, Long> {
//    @Query("""
//            SELECT c, c.user
//            FROM Comment c
//            JOIN c.user
//            WHERE c.review.id = :id
//            """)
//    List<Comment> findAllByReviewId(Long id);
//}