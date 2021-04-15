package com.ntnu.gidd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
      @NotNull
      @NotEmpty
      private String firstName;

      @NotNull
      @NotEmpty
      private String lastName;

      @NotNull
      @NotEmpty
      private String password;
      private String matchingPassword;

      @NotNull
      @NotEmpty
      private String email;
      
      @NotNull
      @NotEmpty
      private LocalDate birthDate;
}