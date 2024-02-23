package com.oc.paymybuddy.service;

import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.PartnershipID;
import com.oc.paymybuddy.model.User;
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

    @Transactional
    public Partnership addPartnership(User user, String partnerEmail) {

        User partner  = userRepository.findByEmail(partnerEmail);

        Partnership partnership = new Partnership();
        PartnershipID partnershipID = new PartnershipID();
        partnershipID.setSenderId(user.getUserId());
        partnershipID.setReceiverId(partner.getUserId());
        partnership.setId(partnershipID);

        logger.info("User with userID {} added partner with userID {}", user.getUserId(), partner.getUserId());
        return partnershipRepository.save(partnership);
    }
    public List<String> getEmailsFromPartners(Long senderID) {

        List<Partnership> partnerships = partnershipRepository.findByIdSenderId(senderID);

        List<Long> receiverIds = partnerships.stream()
                .map(partnership -> partnership.getId().getReceiverId())
                .toList();

        List<String> receiverEmails = userRepository.findEmailsByIds(receiverIds);

        return receiverEmails;
    }

}
