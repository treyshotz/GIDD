package com.ntnu.gidd.model;


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
@EqualsAndHashCode(of={"email"}, callSuper = true)
public class User extends UUIDModel {
    
    private String firstName;
    private String surname;
    @Column(unique = true)
    private String email;
    private LocalDate birthDate;
    private String password;

    @ManyToMany
    @JoinTable(name = "hosts", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "activity_id"}))
    private List<Activity> activities;

    @OneToOne
    @JoinColumn(name = "traning_level_id", referencedColumnName = "id")
    private TrainingLevel trainingLevel;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "invites", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "activity_id"}))
    private List<Activity> invites;
    @OneToOne
    private PasswordResetToken resetToken;

    String image;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false)
    private List<Comment> comments;
    


    @PreRemove
    private void removeActivitiesFromUsers() {
        if(activities != null)
           activities.clear();
    }

}
