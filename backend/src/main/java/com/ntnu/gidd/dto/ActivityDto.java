package com.ntnu.gidd.dto;

import com.ntnu.gidd.model.GeoLocation;
import com.ntnu.gidd.util.TrainingLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    @NotNull
    private UUID id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private LocalDateTime start_date;
    @NotNull
    private LocalDateTime end_date;
    @NotNull
    private LocalDateTime signup_start;
    @NotNull
    private LocalDateTime signup_end;
    @NotNull
    private boolean closed;
    private List<UserListDto> hosts;
    private TrainingLevelEnum level;
    private int capacity;
    private GeoLocation geoLocation;
}
