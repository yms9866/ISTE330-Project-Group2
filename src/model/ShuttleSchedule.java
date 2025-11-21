
/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/18/2025
*/
package model;

import java.sql.Time;

/**
 * Model class representing the SHUTTLE_SCHEDULE table
 * Timetable for each route
 */
public class ShuttleSchedule {
    private int scheduleId;
    private int shuttleId;
    private int routeId;
    private String dayOfWeek; // 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'
    private Time departureTime;
    private Time arrivalTime;
    private boolean isActive;

    // Default constructor
    public ShuttleSchedule() {
        this.isActive = true;
    }

    // Constructor with all fields
    public ShuttleSchedule(int scheduleId, int shuttleId, int routeId, String dayOfWeek, 
                          Time departureTime, Time arrivalTime, boolean isActive) {
        this.scheduleId = scheduleId;
        this.shuttleId = shuttleId;
        this.routeId = routeId;
        this.dayOfWeek = dayOfWeek;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.isActive = isActive;
    }

    // Constructor without scheduleId (for new schedules)
    public ShuttleSchedule(int shuttleId, int routeId, String dayOfWeek, 
                          Time departureTime, Time arrivalTime) {
        this.shuttleId = shuttleId;
        this.routeId = routeId;
        this.dayOfWeek = dayOfWeek;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.isActive = true;
    }

    // Getters and Setters
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getShuttleId() {
        return shuttleId;
    }

    public void setShuttleId(int shuttleId) {
        this.shuttleId = shuttleId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "ShuttleSchedule{" +
                "scheduleId=" + scheduleId +
                ", shuttleId=" + shuttleId +
                ", routeId=" + routeId +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", isActive=" + isActive +
                '}';
    }
}
