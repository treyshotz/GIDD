package com.ntnu.gidd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
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
}