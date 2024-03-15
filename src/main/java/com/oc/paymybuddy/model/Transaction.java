package com.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.text.DecimalFormat;

@Entity
@Data
@Table(name="transaction")
public class Transaction {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column (name="transaction_number")
    private long transactionNumber;

    public void setFee(double fee) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        this.fee = Double.parseDouble(decimalFormat.format(fee));
    }

    public Transaction(Timestamp timestamp, double amount, double fee, String description, long senderUserId, long receiverUserId) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double roundedFee = Double.parseDouble(decimalFormat.format(fee));

        this.timestamp = timestamp;
        this.amount = amount;
        this.fee = roundedFee;
        this.description = description;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
    }

    @Column (name="timestamp")
    private Timestamp timestamp;
    @Column (name="amount")
    private double amount;

    @Column(name="fee")
    private double fee;
    @Column (name="description")
    private String description;
    @Column (name="sender_user_id")
    private long senderUserId;
    @Column (name="receiver_user_id")
    private long receiverUserId;

    public Transaction() {

    }

    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(amount) + "$";
    }
}
