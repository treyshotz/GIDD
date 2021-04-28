package com.ntnu.gidd.repository;

import com.ntnu.gidd.dto.Comment.CommentDto;
import com.ntnu.gidd.model.Comment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CommentRepository extending the JpaRepository, with Comment as type and CommentId as id
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {



}
