package com.ntnu.gidd.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.ZonedDateTime ;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

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
    private ZonedDateTime  startDate;
    @NotNull
    private ZonedDateTime  endDate;
    @NotNull
    private ZonedDateTime  signupStart;
    @NotNull
    private ZonedDateTime  signupEnd;
    @NotNull
    private boolean closed;
    @OneToOne
    @JoinColumn(name = "traning_level_id", referencedColumnName = "id")
    private TrainingLevel trainingLevel;
    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // TODO: this persist transient instances
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

    @Transient
    @QueryType(PropertyType.DATETIME)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime  startDateBefore;

    @Transient
    @QueryType(PropertyType.DATETIME)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime  startDateAfter;

}
