package com.ntnu.gidd.dto;


import lombok.*;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String firstName;
    private String surname;
    @NonNull
    private String email;
    private LocalDate birthDate;
}
