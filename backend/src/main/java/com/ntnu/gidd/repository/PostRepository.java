package com.ntnu.gidd.repository;

import com.ntnu.gidd.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface PostRepository  extends JpaRepository<Post, UUID> , QuerydslPredicateExecutor<Post> {

}
