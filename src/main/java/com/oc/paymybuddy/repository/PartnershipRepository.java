package com.oc.paymybuddy.repository;

import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.PartnershipID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnershipRepository extends JpaRepository<Partnership, PartnershipID> {

    List<Partnership> findByIdSenderId(long senderId);

}