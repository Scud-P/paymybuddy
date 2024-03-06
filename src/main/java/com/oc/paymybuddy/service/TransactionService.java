package com.oc.paymybuddy.service;

import com.oc.paymybuddy.model.DTO.TransactionDTO;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    public UserService userService;

    @Autowired
    public PartnershipService partnershipService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);


    @Transactional
    public Transaction submitTransaction(long senderUserId, String partnerEmail, double amount, String description) {
        try {
            User receiver = userService.findByEmail(partnerEmail);

            if (!partnershipService.isAPartner(senderUserId, partnerEmail)) {
                logger.error("User with email address {} is not a partner of user with ID number {}", partnerEmail, senderUserId);
                throw new IllegalArgumentException("User with email address " + partnerEmail + " is not a partner of user with ID number " + senderUserId);
            }

            if (!userService.hasSufficientBalance(senderUserId, amount)) {
                logger.error("You do not have enough funds to initiate a transaction with amount {} $", amount);
                throw new IllegalArgumentException("User with ID number " + senderUserId + " does not have enough funds to initiate a transaction with amount" + amount);
            }

            userService.setSenderBalance(senderUserId, amount);
            userService.setReceiverBalance(receiver, amount);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Transaction transaction = new Transaction();
            transaction.setSenderUserId(senderUserId);
            transaction.setReceiverUserId(receiver.getUserId());
            transaction.setTimestamp(timestamp);
            transaction.setAmount(amount);
            transaction.setDescription(description);
            transactionRepository.save(transaction);
            return transaction;

        } catch (IllegalArgumentException e) {
            logger.error("An IllegalArgumentException occurred during transaction processing: {}", e.getMessage());
            throw e;
        }
    }


    public Page<TransactionDTO> getRecentTransactionsDTOBySenderUserID(long senderUserId, Pageable pageable) {

        Page<Transaction> transactions = transactionRepository.findRecentBySenderUserId(senderUserId, pageable);
        List<TransactionDTO> transactionDTOS = new ArrayList<>();
        for (Transaction transaction : transactions.getContent()) {
            long receiverUserId = transaction.getReceiverUserId();
            User receiver = userService.findById(receiverUserId);
            TransactionDTO transactionDTO = new TransactionDTO(transaction, receiver);
            transactionDTOS.add(transactionDTO);
        }
        return new PageImpl<>(transactionDTOS, pageable, transactions.getTotalElements());
    }

}
