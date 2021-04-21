package com.ntnu.gidd.service;

import com.ntnu.gidd.model.GeoLocation;
import com.ntnu.gidd.repository.GeoLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GeoLocationServiceImpl implements GeoLocationService{

    @Autowired
    GeoLocationRepository geoLocationRepository;

    @Override
    public GeoLocation saveGeoLocation(GeoLocation geoLocation) {
        geoLocationRepository.save(geoLocation);
        return geoLocation;
    }
}
