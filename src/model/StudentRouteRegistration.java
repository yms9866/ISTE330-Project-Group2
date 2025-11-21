/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/19/2025
*/
package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing the STUDENT_ROUTE_REGISTRATION table
 * Student-route enrollment records
 */
public class StudentRouteRegistration {
    private int registrationId;
    private int studentId;
    private int routeId;
    private Timestamp registrationDate;
    private Date expiryDate;
    private String status; // 'Active', 'Expired', 'Cancelled'
    private BigDecimal creditsPaid;

    // Default constructor
    public StudentRouteRegistration() {
        this.status = "Active";
    }

    // Constructor with all fields
    public StudentRouteRegistration(int registrationId, int studentId, int routeId, 
                                   Timestamp registrationDate, Date expiryDate, 
                                   String status, BigDecimal creditsPaid) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.routeId = routeId;
        this.registrationDate = registrationDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.creditsPaid = creditsPaid;
    }

    // Constructor without registrationId (for new registrations)
    public StudentRouteRegistration(int studentId, int routeId, Date expiryDate, 
                                   String status, BigDecimal creditsPaid) {
        this.studentId = studentId;
        this.routeId = routeId;
        this.expiryDate = expiryDate;
        this.status = status;
        this.creditsPaid = creditsPaid;
    }

    // Getters and Setters
    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getCreditsPaid() {
        return creditsPaid;
    }

    public void setCreditsPaid(BigDecimal creditsPaid) {
        this.creditsPaid = creditsPaid;
    }

    @Override
    public String toString() {
        return "StudentRouteRegistration{" +
                "registrationId=" + registrationId +
                ", studentId=" + studentId +
                ", routeId=" + routeId +
                ", registrationDate=" + registrationDate +
                ", expiryDate=" + expiryDate +
                ", status='" + status + '\'' +
                ", creditsPaid=" + creditsPaid +
                '}';
    }
}
