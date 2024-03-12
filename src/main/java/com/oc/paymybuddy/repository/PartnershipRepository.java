package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.model.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnershipRepository extends JpaRepository<Partnership, Long> {

    List<Partnership> findByOwnerId(long senderUserId);

}