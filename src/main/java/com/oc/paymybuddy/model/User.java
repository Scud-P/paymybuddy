package com.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.text.DecimalFormat;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    public User(String firstName, String lastName, String email, String password, double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    @Column(name = "balance")
    private double balance;

    public User() {

    }

    public String getFormattedBalance() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(balance) + "$";
    }

}


