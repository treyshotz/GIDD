package com.ntnu.gidd.service.post;


import com.ntnu.gidd.dto.post.PostCreateDto;
import com.ntnu.gidd.dto.post.PostDto;
import com.ntnu.gidd.exception.ActivityNotFoundException;
import com.ntnu.gidd.exception.PostNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.Post;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.PostRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{

    PostRepository postRepository;

    ActivityRepository activityRepository;

    UserRepository userRepository;

    ModelMapper modelMapper;


    @Override
    public Page<PostDto> findAllPosts(Predicate predicate, Pageable pageable) {
        return postRepository.findAll(predicate, pageable).map(s -> modelMapper.map(s, PostDto.class));
    }

    @Override
    public PostDto getPostById(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundExecption::new);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto savePost(PostCreateDto post, String email) {
        Post newPost = Post.builder().id(UUID.randomUUID()).content(post.getContent()).image(post.getImage()).build();
        Activity activity = activityRepository.findById(post.getActivityId()).orElse(null);
        newPost.setActivity(activity);
        newPost.setCreator(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
        newPost = postRepository.save(newPost);
        return modelMapper.map(newPost, PostDto.class);
    }

    @Override
    public PostDto updatePost(UUID postId, PostDto updatePost) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundExecption::new);
        post.setContent(updatePost.getContent());
        post.setImage(updatePost.getImage());
        if(updatePost.getActivity() == null) post.setActivity(null);
        else post.setActivity(activityRepository.findById(updatePost.getActivity().getId()).orElseThrow(ActivityNotFoundException::new));

        return modelMapper.map(postRepository.save(post), PostDto.class);
    }

    @Override
    public void deletePost(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundExecption::new);
        postRepository.delete(post);

    }
}
