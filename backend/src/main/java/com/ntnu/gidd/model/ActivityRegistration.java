package com.ntnu.gidd.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name="activity_registration")
public class ActivityRegistration extends UUIDModel{

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    User user;

    @ManyToOne
    @JoinColumn(name="activity_id", referencedColumnName = "id")
    Activity activity;

}
