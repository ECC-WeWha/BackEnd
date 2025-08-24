package com.example.wewha.post.common.domain; // 패키지는 실제 위치에 맞게 수정

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String boardName;

    public Board(String boardName) {
        this.boardName = boardName;
    }
}