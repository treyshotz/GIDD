package com.ntnu.gidd.repository;

import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> , QuerydslPredicateExecutor<User> {
    Page<User> findUserByInvites(Activity activity, Pageable pageable);
    Optional<User> findByEmail(String email);

}
