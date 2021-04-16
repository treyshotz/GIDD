package com.ntnu.gidd.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * Registration is a class for the registration of a user to a activity
 * The unique identifier is a composite key RegistrationId defined by the unique id of the user and activity
 * The class maps the to the user and activity with many to one relations
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name="registration")
public class Registration extends TimeStampedModel{

    @EmbeddedId
    private RegistrationId registrationId;

    @MapsId("user_id")
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    User user;

    @MapsId("activity_id")
    @ManyToOne
    @JoinColumn(name="activity_id", referencedColumnName = "id")
    Activity activity;
}
