package com.ntnu.gidd.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityListDto {
    private UUID id;
    private String title;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private boolean closed;

}
