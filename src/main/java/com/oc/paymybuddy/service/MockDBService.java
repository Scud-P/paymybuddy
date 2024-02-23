package com.oc.paymybuddy.service;

import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.PartnershipID;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.PartnershipRepository;
import com.oc.paymybuddy.repository.TransactionRepository;
import com.oc.paymybuddy.repository.UserRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
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

    private static final Logger logger = LoggerFactory.getLogger(MockDBService.class);

    // Autoincrement must be reset manually before starting the application because of foreign key constraints
    @PostConstruct
    public void populateMockDB() {
        populateMockUserTable();
        populateMockPartnershipTable();
        populateMockTransactionTable();
    }

    // userRepository must be emptied last to maintain Data Integrity
    @PreDestroy
    public void clearMockDB() {
        partnershipRepository.deleteAll();
        transactionRepository.deleteAll();
        userRepository.deleteAll();
        logger.info("Mock database cleared successfully.");

    }

    public void populateMockUserTable() {
        List<User> users = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("mock_users.csv");
        Reader reader = null;
        try {
            reader = new FileReader(resource.getFile());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert reader != null;
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.skip(1);
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    String firstName = line[0];
                    String lastName = line[1];
                    String email = line[2];
                    double balance = Double.parseDouble(line[3]);
                    String password = line[4];

                    User user = new User();
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    user.setBalance(balance);
                    user.setPassword(password);
                    users.add(user);
                    userRepository.save(user);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        logger.info("List of mock users added: {}", users);
    }

    public void populateMockTransactionTable() {
        List<Transaction> mockTransactions = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("mock_transactions.csv");
        Reader reader = null;
        try {
            reader = new FileReader(resource.getFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert reader != null;
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.skip(1);
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    long transactionNumber = Long.parseLong(line[0]);
                    String timestampString = line[1];
                    double amount = Double.parseDouble(line[2]);
                    String description = line[3];
                    long senderId = Long.parseLong(line[4]);
                    long receiverId = Long.parseLong(line[5]);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
                    Date parsedDate = dateFormat.parse(timestampString);
                    Timestamp timestamp = new Timestamp(parsedDate.getTime());

                    Transaction transaction = new Transaction();
                    transaction.setTransactionNumber(transactionNumber);
                    transaction.setTimestamp(timestamp);
                    transaction.setDescription(description);
                    transaction.setAmount(amount);
                    transaction.setSenderUserId(senderId);
                    transaction.setReceiverUserId(receiverId);

                    mockTransactions.add(transaction);

                    transactionRepository.save(transaction);
                }
            }
        } catch (IOException | CsvValidationException | ParseException e) {
            e.printStackTrace();
        }
        logger.info("List of mock transactions added: {}", mockTransactions);
    }

    public void populateMockPartnershipTable() {
        List<Partnership> mockPartnerships = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("mock_partnerships.csv");
        Reader reader = null;
        try {
            reader = new FileReader(resource.getFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert reader != null;
            try (CSVReader csvReader = new CSVReader(reader)) {
                csvReader.skip(1);
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    long senderId = Long.parseLong(line[0]);
                    long receiverId = Long.parseLong(line[1]);
                    PartnershipID partnershipID = new PartnershipID();
                    partnershipID.setSenderId(senderId);
                    partnershipID.setReceiverId(receiverId);
                    Partnership partnership = new Partnership();
                    partnership.setId(partnershipID);
                    mockPartnerships.add(partnership);
                    partnershipRepository.save(partnership);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        logger.info("List of mock relationships added: {}", mockPartnerships);
    }
}

//TODO
// Handle exception