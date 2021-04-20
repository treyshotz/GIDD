package com.ntnu.gidd.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.*;

/**
 * RegistrationId is a class defining the id of a registration
 * The unique id is a composite key combining the userId and activityId
 */

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationId implements Serializable {

  @Column(name = "user_id")
  private UUID userId;

  @Column(name="activity_id")
  private UUID activityId;
}

