package com.ntnu.gidd.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Post  extends UUIDModel{

    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    private Activity activity;

    @Column(columnDefinition = "TEXT")
    private String content;


    @Column(columnDefinition = "TEXT")
    private String image;

}
