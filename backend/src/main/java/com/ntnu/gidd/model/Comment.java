package com.ntnu.gidd.model;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name="comment")
@EqualsAndHashCode(callSuper = true)
public class Comment  extends UUIDModel{

  @Column(columnDefinition = "TEXT")
  @NotNull
  private String comment;

  @ManyToOne
  private User user;


}
