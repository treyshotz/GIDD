package com.ntnu.gidd.repository;

import com.ntnu.gidd.model.Registration;
import com.ntnu.gidd.model.RegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * RegistrationRepository extending the JpaRepository, with Registration as type and RegistrationId as id
 * Contains methods for finding a registration based on the user, activity and registrationId
 */

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, RegistrationId> {
    Optional<List<Registration>> findRegistrationsByActivity_Id(UUID activityId);
    Optional<List<Registration>> findRegistrationsByUser_Id(UUID userId);
    Optional<Registration> findRegistrationByUser_IdAndActivity_Id(UUID userid, UUID activityId);
}
