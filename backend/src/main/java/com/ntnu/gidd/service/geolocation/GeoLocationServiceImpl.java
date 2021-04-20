package com.ntnu.gidd.service.geolocation;

import com.ntnu.gidd.model.GeoLocation;
import com.ntnu.gidd.repository.GeoLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationServiceImpl implements GeoLocationService {

    @Autowired
    GeoLocationRepository geoLocationRepository;

    @Override
    public GeoLocation saveGeoLocation(GeoLocation geoLocation) {
        geoLocationRepository.save(geoLocation);
        return geoLocation;
    }

    /**
     * Method for calculating distance between to positions
     * with Haversine formula
     * Finds radian value of latitude and longitude
     * Finds the sin of latitude and longitude
     * @param startPos
     * @param endPos
     * @return
     */
    @Override
    public double calcDistance(GeoLocation startPos, GeoLocation endPos) {

        double startLatRad = startPos.getLatitude() * Math.PI / 180;
        double startLonRad = startPos.getLongitude() * Math.PI / 180;
        double endLatRad = endPos.getLatitude() * Math.PI / 180;
        double endLonRad = endPos.getLongitude() * Math.PI / 180;

        double sin_latitude = Math.sin((startLatRad - endLatRad) / 2.0);
        double sin_longitude = Math.sin((startLonRad - endLonRad) / 2.0);

        //Calculates with Haversine formula
        return (int) (2 * 6371 * Math.asin(Math.sqrt(
                sin_latitude * sin_latitude + Math.cos(startLatRad) * Math.cos(endLatRad) * sin_longitude * sin_longitude
        )));
    }

    @Override
    public GeoLocation findGeolocation(GeoLocation geoLocation) {
        return geoLocationRepository.findById(geoLocation.getId()).
                orElseThrow();
    }

    @Override
    public GeoLocation findGeolocationByPosition(double latitude, double longitude){
        return geoLocationRepository.findGeoLocationByLatitudeAndLongitude(latitude, longitude)
                .orElseThrow();
    }
}
