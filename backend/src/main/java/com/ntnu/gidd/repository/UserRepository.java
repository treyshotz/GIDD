package com.ntnu.gidd.repository;

import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

}
