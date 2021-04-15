package com.ntnu.gidd.model;


import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name="activity")
public class Activity extends UUIDModel {

    @NotNull
    private String title;
    private String description;
    @NotNull
    private LocalDateTime start_date;
    @NotNull
    private LocalDateTime end_date;
    @NotNull
    private LocalDateTime signup_start;
    @NotNull
    private LocalDateTime signup_end;
    @NotNull
    private boolean closed;
    @OneToOne
    @JoinColumn(name = "traning_level_id", referencedColumnName = "id")
    private TrainingLevel trainingLevel;
    private int capacity;
    //TODO add host: user foreign key
}
