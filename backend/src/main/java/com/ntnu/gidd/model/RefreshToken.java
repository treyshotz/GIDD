package com.ntnu.gidd.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;


@Data
@Entity
public class RefreshToken {
    @Id
    private UUID jti;

    private boolean isValid;

    @OneToOne
    private RefreshToken next;

}
