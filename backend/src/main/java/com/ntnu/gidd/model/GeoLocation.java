package com.ntnu.gidd.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation {

    @Id
    Long id;
    @NotNull
    private String longitude;
    @NotNull
    private String latitude;
}
