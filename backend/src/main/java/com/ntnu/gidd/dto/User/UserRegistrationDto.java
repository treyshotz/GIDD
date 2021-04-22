package com.ntnu.gidd.dto.User;

import com.ntnu.gidd.validation.FieldMatch;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldMatch(field = "password", fieldMatch = "matchingPassword")
public class UserRegistrationDto {
      @NotNull
      @NotEmpty
      private String firstName;

      @NotNull
      @NotEmpty
      private String surname;

      
      @NotNull
      @NotEmpty
      private String password;
      private String matchingPassword;

      @NotNull
      @NotEmpty
      @Email
      private String email;

      private LocalDate birthDate;
}