package com.oc.paymybuddy;

import com.oc.paymybuddy.controller.TransactionController;
import com.oc.paymybuddy.controller.UserController;
import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.PartnershipRepository;
import com.oc.paymybuddy.repository.UserRepository;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(PartnershipService.class)
public class PartnershipServiceTest {

    @MockBean
    public UserRepository userRepository;

    @MockBean
    public PartnershipRepository partnershipRepository;

    @Autowired
    public PartnershipService partnershipService;

    @MockBean
    public UserService userService;

    @MockBean
    public MockDBService mockDBService;

    @MockBean
    public TransactionController transactionController;

    @MockBean
    public UserController userController;

    @Test
    public void testIsAPartner() {

        String email = "email";
        long senderId = 1;

        List<String> partnersEmails = new ArrayList<>();
        partnersEmails.add(email);

        when(partnershipService.getEmailsFromPartners(senderId)).thenReturn(partnersEmails);

        boolean result = partnershipService.isAPartner(senderId, email);

        assertTrue(result);
    }

    @Test
    public void getEmailsFromPartners() {

        long senderId = 1;

        Partnership partnership1 = new Partnership();
        partnership1.setOwnerId(1);
        partnership1.setPartnerId(2);
        partnership1.setPartnershipId(1);


        Partnership partnership2 = new Partnership();
        partnership2.setOwnerId(1);
        partnership2.setPartnerId(3);
        partnership2.setPartnershipId(2);



        List<Partnership> partnerships = new ArrayList<>();
        partnerships.add(partnership1);
        partnerships.add(partnership2);

        when(partnershipRepository.findByOwnerId(senderId)).thenReturn(partnerships);

        List<String> emails = List.of("email1", "email2");
        List<Long> receiverIds = List.of(2L, 3L);

        when(userRepository.findEmailsByIds(receiverIds)).thenReturn(emails);

        List<String> result = partnershipService.getEmailsFromPartners(senderId);

        verify(partnershipRepository).findByOwnerId(senderId);
        verify(userRepository).findEmailsByIds(receiverIds);

        assertEquals(emails, result);
    }

    @Test
    public void testAddPartnership() {

        String partnerEmail = "email";

        User currentUser = new User();
        currentUser.setUserId(1);

        User partner = new User();
        currentUser.setUserId(2);

        when(userRepository.findByEmail(partnerEmail)).thenReturn(partner);

        partnershipService.addPartnership(1L, partnerEmail);

        verify(partnershipRepository, times(1)).save(any(Partnership.class));
    }

    @Test
    public void testAddPartnershipPartnerNotFound() {

        String partnerEmail = "email";

        User currentUser = new User();
        currentUser.setUserId(1L);

        when(userRepository.findByEmail(partnerEmail)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            partnershipService.addPartnership(1L, partnerEmail);
        });
    }

    @Test
    public void testAddPartnershipPartnerAlreadyInList() {

        String partnerEmail = "email";

        User currentUser = new User();
        currentUser.setUserId(1L);

        User partner = new User();
        currentUser.setUserId(2L);

        List<String> partnerEmails = List.of(
                partnerEmail
        );

        when(userRepository.findByEmail(partnerEmail)).thenReturn(partner);
        when(partnershipService.getEmailsFromPartners(1L)).thenReturn(partnerEmails);

        assertThrows(IllegalArgumentException.class, () -> {
            partnershipService.addPartnership(1L, partnerEmail);
        });
    }
}