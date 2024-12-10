package com.deepanshu.service;

import org.springframework.stereotype.Service;

@Service
public class DistanceCalculatorServiceImplementation implements DistanceCalculatorService{

    private static final int EARTH_RADIUS=6371;

    @Override
    public double calculateDistance(double startLat, double startLong, double endLat, double endLong) {
        double dLat = Math.toRadians(endLat - startLat);
        double dLong = Math.toRadians(endLong - startLong);

        double startLatRad = Math.toRadians(startLat);
        double endLatRad = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLatRad) * Math.cos(endLatRad) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
