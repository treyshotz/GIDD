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