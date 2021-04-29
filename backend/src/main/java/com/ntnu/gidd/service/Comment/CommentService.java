package com.ntnu.gidd.service.Comment;


import com.ntnu.gidd.model.Comment;
import java.rmi.activation.ActivationException;
import java.util.List;
import  com.ntnu.gidd.dto.Comment.CommentDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

  CommentDto getCommentById(UUID commentId);
  CommentDto updateComment(UUID commentId, CommentDto commentDto, String creatorEmail, UUID activityId);
  CommentDto saveComment(Comment comment, UUID activityId, String creatorEmail);
  void deleteComment(UUID commentId, String creatorEmail, UUID activityId);
  void deleteAllCommentsOnActivity(UUID activityId, String creatorEmail);
  void deleteAllCommentsOnUser(String creatorEmail);
  Page<CommentDto> getCommentsOnActivity(Pageable pageable, UUID activityId);


}
