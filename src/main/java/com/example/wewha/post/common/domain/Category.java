package com.example.wewha.post.common.domain;

import jakarta.persistence.*;
import com.example.wewha.post.common.domain.Board;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    public Category(Board board, String categoryName) {
        this.board = board;
        this.categoryName = categoryName;
    }
}