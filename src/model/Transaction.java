package model;

import java.sql.Timestamp;

/**
 * Model class representing a financial transaction in the system
 */
public class Transaction {
    private int transactionId;
    private int recieverid;
    private int senderid;
    private double amount;
    private Timestamp transactionDate;
   

    // Default constructor
    public Transaction() {}

    // Constructor with all fields
    public Transaction(int transactionId, int recieverid, int senderid, double amount, Timestamp transactionDate) {
        this.transactionId = transactionId;
        this.recieverid = recieverid;
        this.senderid = senderid;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getRecieverid() {
        return recieverid;
    }

    public void setRecieverid(int recieverid) {
        this.recieverid = recieverid;
    }

    public int getSenderid() {
        return senderid;
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

  

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", recieverid=" + recieverid +
                ", senderid=" + senderid +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                '}';   
    }
}
