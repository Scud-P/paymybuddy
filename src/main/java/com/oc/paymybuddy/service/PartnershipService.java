package com.oc.paymybuddy.service;

import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.PartnershipID;
import com.oc.paymybuddy.repository.PartnershipRepository;
import com.oc.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartnershipService {

    private static final Logger logger = LoggerFactory.getLogger(PartnershipService.class);

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PartnershipRepository partnershipRepository;

    public boolean isAPartner(Long senderId, String partnerEmail) {
        List<String> partnersEmails = getEmailsFromPartners(senderId);
        return partnersEmails.contains(partnerEmail);
    }

    @Transactional
    public Partnership addPartnership(long userId, String partnerEmail) {

        Long partnerId = userRepository.findIdByEmail(partnerEmail);

        if(partnerId == null) {
            logger.warn("The email address {} does not belong to one of our users", partnerEmail);
            throw new IllegalArgumentException("The email address " + partnerEmail + " does not belong to one of our users.");
        }

        List<String> partnerEmails = getEmailsFromPartners(userId);

        if(partnerEmails.contains(partnerEmail)) {
            logger.warn("User with ID {} and email {} is already a connection of user with userId {} ", partnerId, partnerEmail, userId);
            throw new IllegalArgumentException("The person you are trying to add " + "(" + partnerEmail + ") is already in your buddies list");
        }

        Partnership partnership = new Partnership();
        PartnershipID partnershipID = new PartnershipID();
        partnershipID.setSenderId(userId);
        partnershipID.setReceiverId(partnerId);
        partnership.setId(partnershipID);

        logger.info("User with userID {} added partner with userID {}", userId, partnerId);
        return partnershipRepository.save(partnership);
    }

    public List<String> getEmailsFromPartners(Long senderID) {

        List<Partnership> partnerships = partnershipRepository.findByIdSenderUserId(senderID);

        List<Long> receiverIds = partnerships.stream()
                .map(partnership -> partnership.getId().getReceiverId())
                .toList();

        return userRepository.findEmailsByIds(receiverIds);
    }

}
