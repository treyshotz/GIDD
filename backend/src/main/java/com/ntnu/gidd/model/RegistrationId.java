package com.ntnu.gidd.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RegistrationId is a class defining the id of a registration
 * The unique id is a composite key combining the userId and activityId
 */

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor

public class RegistrationId implements Serializable {

  @Column(name = "user_id")
  private UUID userId;

  @Column(name="activity_id")
  private UUID activityId;

}

