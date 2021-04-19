package com.ntnu.gidd.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name="activity")
@EqualsAndHashCode(callSuper = true)
public class Activity extends UUIDModel {

    @NotNull
    private String title;
    @NotNull
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
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "hosts", joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id" ),
    inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"activity_id", "user_id"}))
    private List<User> hosts;
    private int capacity;

    @PreRemove
    private void removeHostsFromActivity() {
        if(this.hosts != null)for (User user : hosts) {
            user.getActivities().remove(this);
        }
    }
}
