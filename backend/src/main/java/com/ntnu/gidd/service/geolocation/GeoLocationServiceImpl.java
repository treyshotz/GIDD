package com.ntnu.gidd.service.geolocation;

import com.ntnu.gidd.model.GeoLocation;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationServiceImpl implements GeoLocationService {

    @Override
    public double calcDistance(GeoLocation startPos, GeoLocation endPos) {

        double startLatRad = startPos.getLatitude() * Math.PI / 180;
        double startLonRad = startPos.getLongitude() * Math.PI / 180;
        double endLatRad = endPos.getLatitude() * Math.PI / 180;
        double endLonRad = endPos.getLongitude() * Math.PI / 180;

        double sin_latitude = Math.sin((startLatRad - endLatRad) / 2.0);
        double sin_longitude = Math.sin((startLonRad - endLonRad) / 2.0);
        return (int) (2 * 6371 * Math.asin(Math.sqrt(
                sin_latitude * sin_latitude + Math.cos(startLatRad) * Math.cos(endLatRad) * sin_longitude * sin_longitude
        )));
    }
}
