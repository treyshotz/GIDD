package com.ntnu.gidd.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends UUIDModel {
    
    private String firstName;
    private String surname;
    @Column(unique = true)
    private String email;
    private LocalDate birthDate;
    private String password;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "hosts", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "activity_id"}))
    private List<Activity> activities;
    @OneToOne
    @JoinColumn(name = "traning_level_id", referencedColumnName = "id")
    private TrainingLevel trainingLevel;

    @PreRemove
    private void removeActivitiesFromUsers() {

        if(this.activities != null)for (Activity activity : activities) {
            activity.getHosts().remove(this);
        }
    }

}
