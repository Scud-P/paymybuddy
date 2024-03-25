package com.oc.paymybuddy;

import com.oc.paymybuddy.service.MockDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PayMyBuddy {

    @Autowired
    private MockDBService mockDBService;

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddy.class, args);
    }

}
