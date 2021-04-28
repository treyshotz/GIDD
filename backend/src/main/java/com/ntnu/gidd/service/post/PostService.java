package com.ntnu.gidd.service.post;

import com.ntnu.gidd.dto.post.PostCreateDto;
import com.ntnu.gidd.dto.post.PostDto;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PostService {

    Page<PostDto> findAllPosts(Predicate predicate, Pageable pageable);
    PostDto getPostById(UUID postId);
    PostDto savePost(PostCreateDto newPost, String email);
    PostDto updatePost(UUID postId, PostDto post);
    void deletePost(UUID postId);
}
