package model;

import java.sql.Timestamp;

/**
 * Model class representing the SHUTTLE table
 * Details of each bus and assigned driver
 */
public class Shuttle {
    private int shuttleId;
    private String shuttleNumber;
    private String licensePlate;
    private int capacity;
    private Integer driverId; // Nullable
    private String status; // 'Active', 'Maintenance', 'Inactive'
    private Timestamp createdAt;

    // Default constructor
    public Shuttle() {
        this.status = "Active";
    }

    // Constructor with all fields
    public Shuttle(int shuttleId, String shuttleNumber, String licensePlate, 
                   int capacity, Integer driverId, String status, Timestamp createdAt) {
        this.shuttleId = shuttleId;
        this.shuttleNumber = shuttleNumber;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.driverId = driverId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Constructor without shuttleId (for new shuttles)
    public Shuttle(String shuttleNumber, String licensePlate, int capacity, Integer driverId, String status) {
        this.shuttleNumber = shuttleNumber;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.driverId = driverId;
        this.status = status;
    }

    // Getters and Setters
    public int getShuttleId() {
        return shuttleId;
    }

    public void setShuttleId(int shuttleId) {
        this.shuttleId = shuttleId;
    }

    public String getShuttleNumber() {
        return shuttleNumber;
    }

    public void setShuttleNumber(String shuttleNumber) {
        this.shuttleNumber = shuttleNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Shuttle{" +
                "shuttleId=" + shuttleId +
                ", shuttleNumber='" + shuttleNumber + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", capacity=" + capacity +
                ", driverId=" + driverId +
                ", status='" + status + '\'' +
                '}';
    }
}
