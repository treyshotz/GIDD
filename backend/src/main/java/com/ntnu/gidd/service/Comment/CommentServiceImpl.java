package com.ntnu.gidd.service.Comment;

import com.ntnu.gidd.dto.Activity.ActivityDto;
import com.ntnu.gidd.dto.Comment.CommentDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.NotHostOrCreatorException;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.Comment;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.CommentRepository;
import com.ntnu.gidd.exception.CommentNotFoundException;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.Activity.ActivityService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class CommentServiceImpl implements CommentService {


  private CommentRepository commentRepository;

  private UserRepository userRepository;

  private ActivityRepository activityRepository;

 private  ModelMapper modelMapper;

 private ActivityService activityService;

  /**
   * Retrieve a comment by ID
   * @param commentId
   * @return a comment
   */
  @Override
  public CommentDto getCommentById(UUID commentId) {
    return modelMapper.map(this.commentRepository.findById(commentId)
        .orElseThrow(CommentNotFoundException::new), CommentDto.class);
  }

  /**
   * Update a given comment
   * @param commentId
   * @param comment
   * @param creatorEmail
   * @param activityId
   * @return updated comment
   */
  @Override
  public CommentDto updateComment(UUID commentId, CommentDto comment, String creatorEmail, UUID activityId) {

    if (checkIfHostOrCreater(activityId, creatorEmail) || checkIfOwnerOfComment(creatorEmail, comment)) {

      Comment updateCommentDto = this.commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
      Activity updateActivity = this.activityRepository.findById(activityId).orElseThrow(ActivityNotFoundExecption::new);

      List<Comment> comments =  new ArrayList<>(updateActivity.getComments());
      comments.remove(updateCommentDto);

      updateCommentDto.setComment(comment.getComment());
      updateCommentDto = commentRepository.save(updateCommentDto);

      comments.add(updateCommentDto);
      updateActivity.setComments(comments);
      this.activityRepository.save(updateActivity);

      return modelMapper.map(updateCommentDto, CommentDto.class);
    }else
      throw new NotHostOrCreatorException();
  }


  /**
   * Saves a given comment
   * @param comment
   * @return the saved comment
   */
  @Override
  public CommentDto saveComment(Comment comment, UUID activityId, String creatorEmail) {

    User user = userRepository.findByEmail(creatorEmail).orElseThrow(UserNotFoundException::new);
    comment.setUser(user);

    Activity updateActivity = this.activityRepository.findById(activityId).orElseThrow(ActivityNotFoundExecption::new);
    List<Comment> comments =  new ArrayList<>(updateActivity.getComments());
    comment.setId(UUID.randomUUID());
    comment = commentRepository.save(comment);
    comments.add(comment);
    updateActivity.setComments(comments);
    this.activityRepository.save(updateActivity);
    return modelMapper.map(comment, CommentDto.class);
  }


  /**
   * Deletes a given comment with given ID
   * @param commentId
   * @param creatorEmail
   * @param activityId
   */
  @Override
  public void deleteComment(UUID commentId, String creatorEmail, UUID activityId) {

    if (checkIfHostOrCreater(activityId, creatorEmail) || checkIfOwnerOfComment(creatorEmail, modelMapper.map(commentRepository.findById(commentId), CommentDto.class))) {
      this.commentRepository.deleteById(commentId);
    } else
      throw new NotHostOrCreatorException();
  }


  /**
   *Deletes all comments on a given activity by activity ID
   * @param activityId
   * @param creatorEmail
   */
  @Override
  @Transactional
  public void deleteAllCommentsOnActivity(UUID activityId, String creatorEmail) {
    if (checkIfHostOrCreater(activityId, creatorEmail)) {

      Activity updateActivity = this.activityRepository.findById(activityId).orElseThrow(ActivityNotFoundExecption::new);

      List<Comment> comments =  new ArrayList<>(updateActivity.getComments());
      comments.clear();
      updateActivity.setComments(comments);
      activityRepository.save(updateActivity);

    }else
      throw new NotHostOrCreatorException();
  }


  /**
   * Retrieve all comments on a given activity
   * @param activityId
   * @param pageable
   * @return List of all comments on activity
   */
  @Override
  public Page<CommentDto>  getCommentsOnActivity(Pageable pageable, UUID activityId) {

    Activity activityToFind = activityRepository.findById(activityId).orElseThrow(ActivityNotFoundExecption::new);
    Page<Comment> comments = new PageImpl<>(activityToFind.getComments(), pageable, activityToFind.getComments().size());
    return comments.map((s-> modelMapper.map(s, CommentDto.class)));

  }


  /**
   * Checks if user is a host or creator of a given activity
   * @param activityId
   * @param creatorEmail
   * @return true/false
   */
  private boolean checkIfHostOrCreater(UUID activityId, String creatorEmail){

    User user = userRepository.findByEmail(creatorEmail).orElseThrow(UserNotFoundException::new);
    Activity activity = activityRepository.findById(activityId).orElseThrow(ActivityNotFoundExecption::new);
    return (activity.getCreator().equals(user) || activity.getHosts().contains(user));
  }

  /**
   * Checks if user is a the writer of a given comment
   *
   * @param email
   * @param commentDto
   * @return true/false
   */
  private boolean checkIfOwnerOfComment(String email, CommentDto commentDto) {
    Comment comment = modelMapper.map(commentDto, Comment.class);
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    return user.getComments().contains(comment);
  }
}
