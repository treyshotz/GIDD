package com.ntnu.gidd.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityListDto {
    private String title;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private boolean closed;

}
