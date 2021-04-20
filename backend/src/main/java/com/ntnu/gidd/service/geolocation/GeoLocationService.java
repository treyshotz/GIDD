package com.ntnu.gidd.service.geolocation;

import com.ntnu.gidd.model.GeoLocation;

public interface GeoLocationService {
    GeoLocation saveGeoLocation(GeoLocation geoLocation);
    double calcDistance(GeoLocation startPos, GeoLocation endPos);
    GeoLocation findGeolocation(GeoLocation geoLocation);
    GeoLocation findGeolocationByPosition(double latitude, double longitude);
}
