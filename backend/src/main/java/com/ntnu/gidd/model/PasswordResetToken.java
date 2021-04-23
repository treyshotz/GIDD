package com.ntnu.gidd.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
public class PasswordResetToken extends UUIDModel {
	
	//60 minutes
	private static final int EXPIRATION = 60;
	
	private String token;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "id")
	private User user;
	
	private ZonedDateTime expirationDate;
	
	
}
