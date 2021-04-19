package com.ntnu.gidd.dto;

import com.ntnu.gidd.util.TrainingLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	@NotNull
	private UUID id;
	@NotNull
	@NotEmpty
	private String firstName;
	@NotNull
	@NotEmpty
	private String surname;
	@NotNull
	@NotEmpty
	private String email;
	private LocalDate birthDate;
	private TrainingLevelEnum level;
}