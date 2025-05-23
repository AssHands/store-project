//package com.ak.store.user.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//@ToString(exclude = { "user", "review" })
//@EqualsAndHashCode(exclude = { "user", "review" })
//@Entity
//@Table(name = "comment_review")
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Review review;
//
//    private String text;
//}
