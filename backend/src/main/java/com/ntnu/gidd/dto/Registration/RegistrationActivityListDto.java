package com.ntnu.gidd.dto.Registration;

import com.ntnu.gidd.dto.Activity.ActivityListDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationActivityListDto {
    ActivityListDto activity;
}
