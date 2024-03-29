package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllBySenderUserId(long senderUserId);

    @Query("SELECT t FROM Transaction t WHERE t.senderUserId = :senderUserId ORDER BY t.timestamp DESC")
    Page<Transaction> findRecentBySenderUserId(long senderUserId, Pageable pageable);
}
