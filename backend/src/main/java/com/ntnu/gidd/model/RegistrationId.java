package com.ntnu.gidd.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor

public class RegistrationId implements Serializable {

  @Column(name = "user_id")
  private UUID userId;

  @Column(name="activity_id")
  private UUID activityId;

}

