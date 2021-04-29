package com.ntnu.gidd.service.post;


import com.ntnu.gidd.dto.post.PostCreateDto;
import com.ntnu.gidd.dto.post.PostDto;
import com.ntnu.gidd.exception.ActivityNotFoundException;
import com.ntnu.gidd.exception.PostNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.Post;
import com.ntnu.gidd.model.QPost;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.PostRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.rating.PostLikeService;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{

    PostRepository postRepository;

    ActivityRepository activityRepository;

    UserRepository userRepository;

    PostLikeService postLikeService;

    ModelMapper modelMapper;


    @Override
    public Page<PostDto> findAllPosts(Predicate predicate, Pageable pageable, String email) {
        Page<PostDto> postDtos = postRepository.findAll(predicate, pageable).map(s -> modelMapper.map(s, PostDto.class));
        return postLikeService.checkListLikes(postDtos, email);
    }

    @Override
    public PostDto getPostById(UUID postId, String email) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundExecption::new);
        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.setHasLiked(postLikeService.hasLiked(email, postDto.getId()));
        return postDto;
    }

    @Override
    public PostDto savePost(PostCreateDto post, String email) {
        Post newPost = Post.builder().id(UUID.randomUUID()).content(post.getContent()).image(post.getImage()).build();
        if (post.getActivityId() != null) {
            Activity activity = activityRepository.findById(post.getActivityId()).orElse(null);
            newPost.setActivity(activity);
        } else {
            newPost.setActivity(null);
        }
        newPost.setCreator(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
        newPost.setLikes(List.of());
        newPost = postRepository.save(newPost);
        PostDto postDto = modelMapper.map(newPost, PostDto.class);
        postDto.setHasLiked(postLikeService.hasLiked(email, postDto.getId()));
        return postDto;
    }

    @Override
    public PostDto updatePost(UUID postId, PostDto updatePost, String email) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundExecption::new);
        post.setContent(updatePost.getContent());
        post.setImage(updatePost.getImage());
        if(updatePost.getActivity() == null) post.setActivity(null);
        else post.setActivity(activityRepository.findById(updatePost.getActivity().getId()).orElseThrow(ActivityNotFoundException::new));

        PostDto postDto = modelMapper.map(postRepository.save(post), PostDto.class);
        postDto.setHasLiked(postLikeService.hasLiked(email, postId));
        return postDto;
    }

    @Override
    public void deletePost(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundExecption::new);
        postRepository.delete(post);

    }
    public Page<PostDto> getPostsLikes(Predicate predicate, Pageable pageable, UUID id){
        QPost post = QPost.post;
        predicate = ExpressionUtils.allOf(predicate, post.likes.any().id.eq(id));
        Page<PostDto> posts = this.postRepository.findAll(predicate, pageable)
                .map(s -> modelMapper.map(s, PostDto.class));

        return postLikeService.checkListLikes(posts,id);
    }

    public Page<PostDto> getPostsLikes(Predicate predicate, Pageable pageable, String email){
        QPost post = QPost.post;
        predicate = ExpressionUtils.allOf(predicate, post.likes.any().email.eq(email));
        Page<PostDto> posts = this.postRepository.findAll(predicate, pageable)
                .map(s -> modelMapper.map(s, PostDto.class));

        return postLikeService.checkListLikes(posts,email);
    }
}
