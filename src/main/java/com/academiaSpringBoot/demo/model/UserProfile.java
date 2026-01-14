package com.academiaSpringBoot.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
@Getter
@Setter
public class UserProfile {
        @Id
        @GeneratedValue
        private Long id;

        @OneToOne
        @JoinColumn(name = "user_id")
        private User user;

        @Column
        private Double weight;

        @Column
        private Double height;

        @Column
        private Integer age;

        @Enumerated(EnumType.STRING)
        private UserSex sex;

        @Enumerated(EnumType.STRING)
        private Goal goal;

        @Enumerated(EnumType.STRING)
        private ExperienceLevel experienceLevel;
    }


