/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/23/2025
*/

package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class representing the SHUTTLE_LOCATION table
 * Real-time shuttle position updates
 */
public class ShuttleLocation {
    private int locationId;
    private int shuttleId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal speedKmh;
    private Integer heading; // 0-359 degrees
    private Timestamp timestamp;

    // Default constructor
    public ShuttleLocation() {
        this.speedKmh = BigDecimal.ZERO;
    }

    // Constructor with all fields
    public ShuttleLocation(int locationId, int shuttleId, BigDecimal latitude,
            BigDecimal longitude, BigDecimal speedKmh,
            Integer heading, Timestamp timestamp) {
        this.locationId = locationId;
        this.shuttleId = shuttleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedKmh = speedKmh;
        this.heading = heading;
        this.timestamp = timestamp;
    }

    // Constructor without locationId (for new location updates)
    public ShuttleLocation(int shuttleId, BigDecimal latitude, BigDecimal longitude,
            BigDecimal speedKmh, Integer heading) {
        this.shuttleId = shuttleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speedKmh = speedKmh;
        this.heading = heading;
    }

    // Getters and Setters
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getShuttleId() {
        return shuttleId;
    }

    public void setShuttleId(int shuttleId) {
        this.shuttleId = shuttleId;
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

    public BigDecimal getSpeedKmh() {
        return speedKmh;
    }

    public void setSpeedKmh(BigDecimal speedKmh) {
        this.speedKmh = speedKmh;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ShuttleLocation{" +
                "locationId=" + locationId +
                ", shuttleId=" + shuttleId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", speedKmh=" + speedKmh +
                ", heading=" + heading +
                ", timestamp=" + timestamp +
                '}';
    }
}
