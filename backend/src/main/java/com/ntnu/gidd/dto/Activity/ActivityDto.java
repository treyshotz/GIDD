package com.ntnu.gidd.dto.Activity;

import com.ntnu.gidd.dto.ActivityImageDto;
import com.ntnu.gidd.dto.User.UserListDto;
import com.ntnu.gidd.util.TrainingLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {

    private UUID id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private ZonedDateTime startDate;
    @NotNull
    private ZonedDateTime endDate;
    @NotNull
    private ZonedDateTime signupStart;
    @NotNull
    private ZonedDateTime signupEnd;
    @NotNull
    private UserListDto creator;
    private boolean closed;
    @NotNull
    private List<UserListDto> hosts;
    private TrainingLevelEnum level;
    private int capacity;
    private List<ActivityImageDto> images;
}
