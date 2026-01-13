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

    private String imageUrl;

    @OneToMany(mappedBy = "exercise")
    private List<WorkoutExercise> workouts;

    public Exercise(Object o, String s, String s1, String description) {
    }
}
