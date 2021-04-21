package com.ntnu.gidd.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEmailDto {
    private String email;
    private UUID id;
}
