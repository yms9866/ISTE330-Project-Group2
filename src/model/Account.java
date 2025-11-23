/*
Group 2
Members: Yosef Shibele, Syed Zain, Ismail Mohammed Habibi
Date: 11/23/2025
*/

package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class representing the ACCOUNTS table
 * Stores student ride credits
 */
public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;
    private Timestamp lastUpdated;

    // Default constructor
    public Account() {
        this.balance = BigDecimal.ZERO;
    }

    // Constructor with all fields
    public Account(int accountId, int userId, BigDecimal balance, Timestamp lastUpdated) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.lastUpdated = lastUpdated;
    }

    // Constructor without accountId (for new accounts)
    public Account(int userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
