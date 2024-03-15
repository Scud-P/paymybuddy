package com.oc.paymybuddy.service;

import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.PartnershipRepository;
import com.oc.paymybuddy.repository.TransactionRepository;
import com.oc.paymybuddy.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MockDBService {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PartnershipRepository partnershipRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    private Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(MockDBService.class);

    // User table must be populated first due to other tables using foreign keys

    @PostConstruct
    public void populateMockDB() {
        populateMockUserTable();
        populateMockPartnershipTable();
        populateMockTransactionTable();
    }

    // userRepository must be emptied last to maintain Data Integrity
    @PreDestroy
    public void clearMockDB () {
        partnershipRepository.deleteAll();
        transactionRepository.deleteAll();
        userRepository.deleteAll();
        logger.info("Mock database cleared successfully.");
        userRepository.resetAutoIncrement();
        transactionRepository.resetAutoIncrement();
        partnershipRepository.resetAutoIncrement();
        logger.info("Auto incremented fields reset successfully");
    }

    private void populateMockUserTable() {

        List<User> mockUsers = List.of(
                new User("John", "Smith", "john.smith@example.com", "12345", 0),
                new User("Emily", "Johnson", "emily.johnson@example.com", "12345", 10),
                new User("Michael", "Williams", "michael.williams@example.com", "12345", 20),
                new User("Sarah", "Brown", "sarah.brown@example.com", "12345", 30),
                new User("Christopher", "Jones", "christopher.jones@example.com", "12345", 40),
                new User("Jessica", "Miller", "jessica.miller@example.com", "12345", 50),
                new User("Matthew", "Davis", "matthew.davis@example.com", "12345", 60),
                new User("Ashley", "Garcia", "ashley.garcia@example.com", "12345", 70),
                new User("William", "Rodriguez", "william.rodriguez@example.com", "12345", 80),
                new User("Amanda", "Martinez", "amanda.martinez@example.com", "12345", 90)
        );
        userRepository.saveAll(mockUsers);
        logger.info("Mock users saved: {}", mockUsers);
    }

    private void populateMockTransactionTable() {

        String timestampString1 = "2000-01-01 00:00:00.00";
        String timestampString2 = "2000-01-02 00:00:00.00";
        String timestampString3 = "2000-01-03 00:00:00.00";
        String timestampString4 = "2000-01-04 00:00:00.00";
        String timestampString5 = "2000-01-05 00:00:00.00";
        String timestampString6 = "2000-01-06 00:00:00.00";
        String timestampString7 = "2000-01-07 00:00:00.00";
        String timestampString8 = "2000-01-08 00:00:00.00";
        String timestampString9 = "2000-01-09 00:00:00.00";
        String timestampString10 = "2000-01-10 00:00:00.00";
        String timestampString11 = "2000-01-11 00:00:00.00";
        String timestampString12 = "2000-01-12 00:00:00.00";
        String timestampString13 = "2000-01-13 00:00:00.00";
        String timestampString14 = "2000-01-14 00:00:00.00";
        String timestampString15 = "2000-01-15 00:00:00.00";
        String timestampString16 = "2000-01-16 00:00:00.00";
        String timestampString17 = "2000-01-17 00:00:00.00";
        String timestampString18 = "2000-01-18 00:00:00.00";
        String timestampString19 = "2000-01-19 00:00:00.00";
        String timestampString20 = "2000-01-20 00:00:00.00";

        List<String> timestampStrings = List.of(
                timestampString1, timestampString2, timestampString3, timestampString4, timestampString5, timestampString6, timestampString7,
                timestampString8, timestampString9, timestampString10, timestampString11, timestampString12, timestampString13,
                timestampString14, timestampString15, timestampString16, timestampString17, timestampString18, timestampString19, timestampString20
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");

        List<Timestamp> timestamps = new ArrayList<>();
        List<Transaction> mockTransactions = new ArrayList<>();
        double[] amounts = {5, 5, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        double[] fees = new double[20];

        for(int i = 0; i <amounts.length; i++) {
            double fee = amounts[i] * 0.005;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            double roundedFee = Double.parseDouble(decimalFormat.format(fee));
            fees[i] = roundedFee;
        }

        String[] descriptions = {"Beer", "Wine", "Pastis", "Rhum", "Ti Punch", "Margarita", "Negroni", "White Russian", "Bloody Caesar", "Champagne",
        "Pack of Smokes", "Pack of Beer", "Movie", "Bottle of Wine", "Bottle of Rum", "Bottle of Tequila", "Bottle of Cognac", "Bottle of Champagne",
        "Bottle of Whisky", "Slip Kangourou en Soie"};
        long[] senderIds = {1,2,3,4,5,6,1,2,3,1,1,1,1,1,1,1,1,1,1,1};
        long[] receiverIds = {3,5,4,3,2,4,5,1,2,4,2,3,4,5,2,3,4,5,2,3};

        for (String timestampString : timestampStrings) {
            try {
                Date parsedDate = dateFormat.parse(timestampString);
                Timestamp timestamp = new Timestamp(parsedDate.getTime());
                timestamps.add(timestamp);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < timestamps.size(); i++) {
            Transaction transaction = new Transaction(timestamps.get(i), amounts[i], fees[i], descriptions[i], senderIds[i], receiverIds[i]);
            mockTransactions.add(transaction);
        }
        transactionRepository.saveAll(mockTransactions);
        logger.info("Mock transactions saved: {}", mockTransactions);
    }

    private void populateMockPartnershipTable() {

        List<Partnership> mockPartnerships = List.of(
                new Partnership(1,2), new Partnership(1,3),
                new Partnership(1,4), new Partnership(1,5),
                new Partnership(1,6), new Partnership(2,1),
                new Partnership(2,3), new Partnership(2,4),
                new Partnership(2,5), new Partnership(2,6),
                new Partnership(3,1), new Partnership(3,2),
                new Partnership(3,4), new Partnership(3,5),
                new Partnership(3,6), new Partnership(4,1),
                new Partnership(4,2), new Partnership(4,3),
                new Partnership(4,5), new Partnership(4,6),
                new Partnership(5,1), new Partnership(5,2),
                new Partnership(5,3), new Partnership(5,4),
                new Partnership(5,6), new Partnership(6,1),
                new Partnership(6,2), new Partnership(6,3),
                new Partnership(6,4), new Partnership(6,5)
                );
        partnershipRepository.saveAll(mockPartnerships);
        logger.info("Mock partnerships saved: {}", mockPartnerships);
    }
}