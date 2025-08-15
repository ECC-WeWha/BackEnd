package com.example.wewha.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long regionId;

    @Column(name = "name", nullable = false)
    private String name;
}
