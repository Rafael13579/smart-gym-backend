package com.academiaSpringBoot.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sets")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private int reps;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

}
