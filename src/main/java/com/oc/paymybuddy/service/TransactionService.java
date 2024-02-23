package com.oc.paymybuddy.service;

import com.oc.paymybuddy.model.DTO.TransactionDTO;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    public UserService userService;

    public Transaction submitTransaction(HttpSession session, String email , double amount, String description) {

        if(userService.hasSufficientBalance(session, amount)) {
            long senderUserId = (long) session.getAttribute("userId");
            User receiver = userService.findByEmail(email);
            long receiverUserId = receiver.getUserId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Transaction transaction = new Transaction();
            transaction.setSenderUserId(senderUserId);
            transaction.setReceiverUserId(receiverUserId);
            transaction.setTimestamp(timestamp);
            transaction.setAmount(amount);
            transaction.setDescription(description);
            userService.setSenderBalance(session, amount);
            userService.setReceiverBalance(receiver,amount);
            transactionRepository.save(transaction);
            return transaction;
        }
        return null;
    }


    public List<Transaction> getAllTransactionsBySenderUserID(long senderUserId) {
        return transactionRepository.findAllBySenderUserId(senderUserId);
    }

    public Page<Transaction> getRecentTransactionsBySenderUserID(long senderUserId, Pageable pageable) {
        return transactionRepository.findRecentBySenderUserId(senderUserId, pageable);
    }

}
