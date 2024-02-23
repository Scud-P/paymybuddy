package com.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name="transaction")
public class Transaction {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column (name="transaction_number")
    private long transactionNumber;
    @Column (name="timestamp")
    private Timestamp timestamp;
    @Column (name="amount")
    private double amount;
    @Column (name="description")
    private String description;
    @Column (name="sender_user_id")
    private long senderUserId;
    @Column (name="receiver_user_id")
    private long receiverUserId;

    public Transaction() {

    }
}
