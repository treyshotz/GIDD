package com.ntnu.gidd.repository;

import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ActivityRepository extending JpaRepository with Activity and UUID as id
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    Optional<Activity> findActivityByIdAndHosts_Id(UUID id, UUID hosts_id);
}
