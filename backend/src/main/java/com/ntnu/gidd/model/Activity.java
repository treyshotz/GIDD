package com.ntnu.gidd.model;


import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="activity")
public class Activity extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID activity_id;
    private String title;
    private String description;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private LocalDateTime signup_start;
    private LocalDateTime signup_end;
    private boolean closed;
    private int capacity;
    //TODO add host: user foreign key
}
