package com.ntnu.gidd.repository;

import com.ntnu.gidd.model.GeoLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoLocationRepository extends JpaRepository<GeoLocation, Long> {
}
