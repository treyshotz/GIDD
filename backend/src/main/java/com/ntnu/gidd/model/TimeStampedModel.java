package com.ntnu.gidd.model;


import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class TimeStampedModel {
    @CreatedDate
    private ZonedDateTime created_at;
    @LastModifiedDate
    private ZonedDateTime updated_at;
}
