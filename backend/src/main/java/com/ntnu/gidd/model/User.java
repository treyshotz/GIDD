package com.ntnu.gidd.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class User extends UUIDModel {

    private String firstName;
    private String surname;

    @Column(unique = true)
    private String email;
    private LocalDate birthDate;
    private String password;
    private String salt;

}
