package com.ntnu.gidd.model;


import com.ntnu.gidd.util.TrainingLevelEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TrainingLevel {
    @Id
    Long id;
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private TrainingLevelEnum level;
}
