package com.ntnu.gidd.dto;


import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEmailDto {
    private String email;
    private UUID id;
}
