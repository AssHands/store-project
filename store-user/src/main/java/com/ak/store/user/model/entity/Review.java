//package com.ak.store.user.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//@ToString(exclude = { "user" })
//@EqualsAndHashCode(exclude = { "user" })
//@Entity
//public class Review {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;
//
//    @Builder.Default
//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();
//
//    private Long productId;
//
//    private String text;
//
//    private String advantages;
//
//    private String disadvantages;
//
//    private Integer grade;
//}
