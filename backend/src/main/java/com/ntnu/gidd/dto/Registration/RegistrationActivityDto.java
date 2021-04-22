package com.ntnu.gidd.dto.Registration;

import com.ntnu.gidd.dto.Activity.ActivityDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationActivityDto {
    ActivityDto activity;
}
