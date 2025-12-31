package com.academiaSpringBoot.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String muscularGroup;

    @Column(nullable = false)
    private String description;

    @JoinColumn(name = "workout_id", nullable = false)
    @ManyToOne
    private Workout workout;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingSet> sets;


}
