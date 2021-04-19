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
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class User extends UUIDModel {

    private String firstName;
    private String surname;
    @Column(unique = true)
    private String email;
    private LocalDate birthDate;
    private String password;
    private String salt;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "hosts", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "activity_id"}))
    private List<Activity> activities;

    @PreRemove
    private void removeActivitiesFromUsers() {

        if(this.activities != null)for (Activity activity : activities) {
            activity.getHosts().remove(this);
        }
    }

}
