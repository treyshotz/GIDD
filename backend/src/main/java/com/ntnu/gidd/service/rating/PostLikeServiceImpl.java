package com.ntnu.gidd.service.rating;

import com.ntnu.gidd.dto.post.PostDto;
import com.ntnu.gidd.exception.PostNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Post;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.PostRepository;
import com.ntnu.gidd.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PostLikeServiceImpl implements PostLikeService {

    UserRepository userRepository;

    PostRepository postRepository;


    @Override
    public boolean hasLiked(String email, UUID postId) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null && post !=null){
            return post.getLikes().contains(user);
        }
        return false;
    }

    @Override
    public boolean addLike(String email, UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundExecption::new);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if(user != null && post !=null){
            if(post.getLikes().contains(user))return true;
            List<User> likes = post.getLikes();
            likes.add(user);
            post.setLikes(likes);
            postRepository.save(post);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLike(String email, UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundExecption::new);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if(user != null && post !=null){
            List<User> likes = post.getLikes();
            likes.remove(user);
            post.setLikes(likes);
            postRepository.save(post);
            return false;
        }
        return true;
    }

    @Override
    public Page<PostDto> checkListLikes(Page<PostDto> posts, String email) {
        posts.forEach(s -> s.setHasLiked(hasLiked(email, s.getId())));
        return posts;
    }

    @Override
    public Page<PostDto> checkListLikes(Page<PostDto> posts, UUID id) {
        posts.forEach(s -> s.setHasLiked(hasLiked(id, s.getId())));
        return posts;
    }

    @Override
    public boolean hasLiked(UUID userId, UUID postId) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if(user != null && post !=null){
            return post.getLikes().contains(user);
        }
        return false;
    }
}
