package com.ntnu.gidd.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "post")
public class Post  extends UUIDModel{

    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id", columnDefinition = "CHAR(32)")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    private Activity activity;

    @Column(columnDefinition = "TEXT")
    private String content;


    @Column(columnDefinition = "TEXT")
    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_rating", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}))
    private List<User> likes = new ArrayList<>();


    @Transient
    public int getLikesCount(){
        return likes.size();
    }


    @PreRemove
    public void removeRelationships(){
        clearCreator();
        clearLikes();
    }

    public void clearLikes(){
        if(likes != null)
            likes.clear();
    }

    public void clearCreator(){
        if(creator != null)
            creator = null;
    }
}
