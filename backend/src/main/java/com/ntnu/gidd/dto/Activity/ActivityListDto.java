package com.ntnu.gidd.dto.Activity;


import com.ntnu.gidd.dto.User.UserListDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityListDto {
    private UUID id;
    private String title;
    private String description;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private UserListDto creator;
    private boolean closed;
}
