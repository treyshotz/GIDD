package com.ntnu.gidd.repository;

import com.ntnu.gidd.model.ActivityRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, UUID> {
}
