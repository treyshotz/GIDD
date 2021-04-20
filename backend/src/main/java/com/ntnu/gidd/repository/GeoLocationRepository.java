package com.ntnu.gidd.repository;

import com.ntnu.gidd.model.GeoLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeoLocationRepository extends JpaRepository<GeoLocation, Long> {
    Optional<GeoLocation> findGeoLocationByLatitudeAndLongitude(double latitude, double longitude);
}
