/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/23/2025
*/

package model;

import java.math.BigDecimal;
import java.sql.Time;

/**
 * Model class representing the STOP table
 * Individual stops linked to routes
 */
public class Stop {
    private int stopId;
    private int routeId;
    private String stopName;
    private int stopOrder;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Time estimatedArrivalTime;

    // Default constructor
    public Stop() {
    }

    // Constructor with all fields
    public Stop(int stopId, int routeId, String stopName, int stopOrder,
            BigDecimal latitude, BigDecimal longitude, Time estimatedArrivalTime) {
        this.stopId = stopId;
        this.routeId = routeId;
        this.stopName = stopName;
        this.stopOrder = stopOrder;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    // Constructor without stopId (for new stops)
    public Stop(int routeId, String stopName, int stopOrder,
            BigDecimal latitude, BigDecimal longitude, Time estimatedArrivalTime) {
        this.routeId = routeId;
        this.stopName = stopName;
        this.stopOrder = stopOrder;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    // Getters and Setters
    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public int getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(int stopOrder) {
        this.stopOrder = stopOrder;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Time getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(Time estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "stopId=" + stopId +
                ", routeId=" + routeId +
                ", stopName='" + stopName + '\'' +
                ", stopOrder=" + stopOrder +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", estimatedArrivalTime=" + estimatedArrivalTime +
                '}';
    }
}
