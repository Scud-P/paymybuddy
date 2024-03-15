package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.model.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PartnershipRepository extends JpaRepository<Partnership, Long> {

    List<Partnership> findByOwnerId(long senderUserId);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE partnership AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}