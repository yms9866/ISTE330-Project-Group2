/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/18/2025
*/

package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class representing the ROUTE table
 * Defines shuttle routes and their basic info
 */
public class Route {
    private int routeId;
    private String routeName;
    private String routeCode;
    private String description;
    private BigDecimal distanceKm;
    private Integer estimatedDurationMinutes;
    private BigDecimal creditsRequired;
    private boolean isActive;
    private Timestamp createdAt;

    // Default constructor
    public Route() {
        this.creditsRequired = new BigDecimal("1.00");
        this.isActive = true;
    }

    // Constructor with all fields
    public Route(int routeId, String routeName, String routeCode, String description, 
                 BigDecimal distanceKm, Integer estimatedDurationMinutes, 
                 BigDecimal creditsRequired, boolean isActive, Timestamp createdAt) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeCode = routeCode;
        this.description = description;
        this.distanceKm = distanceKm;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.creditsRequired = creditsRequired;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Constructor without routeId (for new routes)
    public Route(String routeName, String routeCode, String description, 
                 BigDecimal distanceKm, Integer estimatedDurationMinutes, 
                 BigDecimal creditsRequired) {
        this.routeName = routeName;
        this.routeCode = routeCode;
        this.description = description;
        this.distanceKm = distanceKm;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.creditsRequired = creditsRequired;
        this.isActive = true;
    }

    // Getters and Setters
    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public BigDecimal getCreditsRequired() {
        return creditsRequired;
    }

    public void setCreditsRequired(BigDecimal creditsRequired) {
        this.creditsRequired = creditsRequired;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId=" + routeId +
                ", routeName='" + routeName + '\'' +
                ", routeCode='" + routeCode + '\'' +
                ", distanceKm=" + distanceKm +
                ", estimatedDurationMinutes=" + estimatedDurationMinutes +
                ", creditsRequired=" + creditsRequired +
                ", isActive=" + isActive +
                '}';
    }
}
