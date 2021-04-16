package com.ntnu.gidd.factories;

import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.model.User;
import org.springframework.beans.factory.FactoryBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.ntnu.gidd.utils.StringRandomizer.getRandomString;

public class UserRegistrationFactory implements FactoryBean<UserRegistrationDto> {
      Random random = new Random();

      @Override
      public UserRegistrationDto getObject() throws Exception {
            String password = getRandomString(8);
            return UserRegistrationDto.builder()
                  .firstName(getRandomString(6))
                  .surname(getRandomString(6))
                  .password(password)
                  .matchingPassword(password)
                  .email("test" + random.nextInt() + "@mail.no")
                  .birthDate(LocalDate.now())
                  .build();
      }

      @Override
      public Class<?> getObjectType() {
            return null;
      }

      @Override
      public boolean isSingleton() {
            return false;
      }
}