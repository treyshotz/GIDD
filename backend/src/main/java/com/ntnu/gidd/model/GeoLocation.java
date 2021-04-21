package com.ntnu.gidd.model;

import lombok.*;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation extends UUIDModel{

    @NotNull
    private String longitude;
    @NotNull
    private String latitude;
}
