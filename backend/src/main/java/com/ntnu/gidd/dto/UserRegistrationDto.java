package com.ntnu.gidd.dto;

import com.ntnu.gidd.validation.PasswordMatches;
import com.ntnu.gidd.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@PasswordMatches
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

      @ValidEmail
      @NotNull
      @NotEmpty
      private String email;
      
      @NotNull
      @NotEmpty
      private LocalDate birthDate;
}