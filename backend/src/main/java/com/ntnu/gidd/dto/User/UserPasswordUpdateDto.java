package com.ntnu.gidd.dto.User;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdateDto {
	@NonNull
	private String oldPassword;
	@NonNull
	private String newPassword;
}
