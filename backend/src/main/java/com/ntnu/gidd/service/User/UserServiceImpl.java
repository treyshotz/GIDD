package com.ntnu.gidd.service.User;

import com.ntnu.gidd.dto.Registration.RegistrationUserDto;
import com.ntnu.gidd.dto.User.UserDto;
import com.ntnu.gidd.dto.User.UserPasswordResetDto;
import com.ntnu.gidd.dto.User.UserPasswordUpdateDto;
import com.ntnu.gidd.dto.User.UserRegistrationDto;
import com.ntnu.gidd.exception.*;
import com.ntnu.gidd.model.*;
import com.ntnu.gidd.repository.PasswordResetTokenRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.Comment.CommentService;
import com.ntnu.gidd.service.Email.EmailService;
import com.ntnu.gidd.service.Registration.RegistrationService;
import com.ntnu.gidd.util.TrainingLevelEnum;
import com.querydsl.core.types.Predicate;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@NoArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private CommentService commentService;
	
	@Override
	public UserDto getUserDtoByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);
	}
	
	private User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
	}

	@Override
	public User getUserById(UUID id) {
		return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
	}

	private boolean emailExist(String email) {
		return userRepository.findByEmail(email).isPresent();
	}
	
	@Override
	public UserDto saveUser(UserRegistrationDto user) {
		if (emailExist(user.getEmail())) {
			throw new EmailInUseException();
		}
		
		User userObj = modelMapper.map(user, User.class);
		userObj.setPassword(passwordEncoder.encode(user.getPassword()));
		return modelMapper.map(userRepository.save(userObj), UserDto.class);
	}
	
	@Override
	public void changePassword(Principal principal, UserPasswordUpdateDto user) {
		User userObj = getUserByEmail(principal.getName());
		userRepository.flush();
		if (passwordEncoder.matches(user.getOldPassword(), userObj.getPassword())) {
			userObj.setPassword(passwordEncoder.encode(user.getNewPassword()));
			userRepository.save(userObj);
		} else {
			throw new PasswordIsIncorrectException();
		}
	}

	@Override
	public Page<UserDto> getAll(Predicate predicate, Pageable pageable) {
		return userRepository.findAll(predicate, pageable).map(s -> modelMapper.map(s, UserDto.class));
	}

	@Override
	public UserDto getUserByUUID(UUID userId) {
		return modelMapper.map(userRepository.findById(userId)
				.orElseThrow(UserNotFoundException::new),
				UserDto.class);
	}

	@Override
	public UserDto deleteUser(String username) {
		User user = userRepository.findByEmail(username).
				orElseThrow(UserNotFoundException::new);

		registrationService.deleteAllRegistrationsWithUsername(username);
		commentService.deleteAllCommentsOnUser(username);
		userRepository.deleteById(user.getId());

		return modelMapper.map(user, UserDto.class);
	}
	
	@Autowired
	private TrainingLevelRepository trainingLevelRepository;
	
	public UserDto updateUser(UUID id, UserDto user) {
		User updatedUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		updatedUser.setFirstName(user.getFirstName());
		updatedUser.setSurname(user.getSurname());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setBirthDate(user.getBirthDate());
		updatedUser.setImage(user.getImage());
		if (user.getLevel() != null) updatedUser.setTrainingLevel(getTrainingLevel(user.getLevel()));
		return modelMapper.map(userRepository.save(updatedUser), UserDto.class);
	}
	
	private TrainingLevel getTrainingLevel(TrainingLevelEnum level) {
		return trainingLevelRepository.findTrainingLevelByLevel(level).
				orElseThrow(() -> new EntityNotFoundException("Training level does not exist"));
	}
	
	/**
	 * Finds a user, creates a token with the user and saves this.
	 * Sends a email to the user's email containing a link for resetting password
	 *
	 * @param email
	 * @return
	 */
	@SneakyThrows
	@Transactional
	public void forgotPassword(String email){
		User user = getUserByEmail(email);
		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setUser(user);
		UUID id = passwordResetTokenRepository.save(passwordResetToken).getId();
		Map<String, Object> properties = new HashMap<>();
		properties.put("name", user.getFirstName() + " " + user.getSurname());
		properties.put("url", "https://gidd-idatt2106.web.app/auth/reset-password/" +id.toString() + "/");
		
		Mail mail = Mail.builder()
				.from("baregidd@gmail.com")
				.to(user.getEmail())
				.htmlTemplate(new HtmlTemplate("reset_password", properties))
				.subject("Reset password")
				.build();
		emailService.sendEmail(mail);
	}
	
	/**
	 * Finds a user and finds the token
	 * Checks if the token is valid and that the token is connected to the user
	 * If the checks pass, it sets a new password for the user
	 * Returns error if it doesn't pass the checks
	 *
	 * @param userReset
	 */
	public void validateResetPassword(UserPasswordResetDto userReset, UUID passwordResetTokenId) {
		User user = getUserByEmail(userReset.getEmail());
		
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findById(passwordResetTokenId).orElseThrow(ResetPasswordTokenNotFoundException::new);
		
		if (!user.equals(passwordResetToken.getUser()) && !validateResetToken(passwordResetToken)) {
			throw new InvalidResetPasswordToken();
		}
		user.setPassword(passwordEncoder.encode(userReset.getNewPassword()));
		userRepository.save(user);
	}
	
	private boolean validateResetToken(PasswordResetToken token) {
		return token.getExpirationDate().isBefore(ZonedDateTime.now());
	}
}
