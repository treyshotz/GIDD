package com.ntnu.gidd.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class GeoLocation extends UUIDModel{

    @NotNull
    private String longitude;
    @NotNull
    private String latitude;
}
