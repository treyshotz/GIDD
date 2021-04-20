package com.ntnu.gidd.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class GeoLocation{

    @Id
    Long id;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
}
