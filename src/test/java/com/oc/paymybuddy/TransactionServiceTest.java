package com.oc.paymybuddy;

import com.oc.paymybuddy.model.DTO.TransactionDTO;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.TransactionRepository;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockHttpSession;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(TransactionService.class)
public class TransactionServiceTest {

    @MockBean
    public TransactionRepository transactionRepository;

    @MockBean
    public MockDBService mockDBService;

    @MockBean
    public UserService userService;

    @MockBean
    public PartnershipService partnershipService;

    @Autowired
    public TransactionService transactionService;

    @Test
    public void testGetRecentTransactionsDTOBySenderUserID() {

        long senderUserId = 1;

        Pageable pageable = PageRequest.of(0, 3);

        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setSenderUserId(1);
        transaction1.setReceiverUserId(2);
        transaction1.setDescription("Cocktail");
        transaction1.setAmount(10.0);

        Transaction transaction2 = new Transaction();
        transaction2.setSenderUserId(1);
        transaction2.setReceiverUserId(2);
        transaction2.setDescription("Paquet de clopes");
        transaction2.setAmount(12.0);

        Transaction transaction3 = new Transaction();
        transaction3.setSenderUserId(1);
        transaction3.setReceiverUserId(2);
        transaction3.setDescription("Kebab frites");
        transaction3.setAmount(6.0);

        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        when(userService.findById(anyLong())).thenReturn(new User());


        when(transactionRepository.findRecentBySenderUserId(anyLong(), eq(pageable)))
                .thenReturn(new PageImpl<>(transactions, pageable, transactions.size()));

        Page<TransactionDTO> result = transactionService.getRecentTransactionsDTOBySenderUserID(senderUserId, pageable);

        assertEquals(transactions.size(), result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(Objects::nonNull));

    }

    @Test
    public void testSubmitTransaction() {

        long senderID = 1L;
        String partnerEmail = "email";
        String description = "description";
        double amount = 100.0;

        User receiver = new User();
        receiver.setEmail(partnerEmail);
        receiver.setUserId(2L);
        receiver.setFirstName("John");
        receiver.setLastName("Lennon");
        receiver.setBalance(0.0);
        receiver.setPassword("12345");

        User sender = new User();
        sender.setUserId(senderID);
        sender.setBalance(200);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", sender);

        when(userService.findByEmail(partnerEmail)).thenReturn(receiver);

        when(partnershipService.isAPartner(anyLong(), anyString())).thenReturn(true);

        when(userService.hasSufficientBalance(anyLong(), anyDouble())).thenReturn(true);

        Transaction result = transactionService.submitTransaction(senderID, receiver.getEmail(), amount, description);

        assertNotNull(result);
        assertEquals(senderID, result.getSenderUserId());
        assertEquals(receiver.getUserId(), result.getReceiverUserId());
        assertEquals(description, result.getDescription());
        assertEquals(amount, result.getAmount());

        verify(transactionRepository, times(1)).save(any(Transaction.class));

        verify(userService, times(1)).setSenderBalance(1L, 100.5);
        verify(userService, times(1)).setReceiverBalance(receiver, 100.0);
        verify(partnershipService, times(1)).isAPartner(senderID, partnerEmail);
        verify(userService, times(1)).hasSufficientBalance(senderID, 100);
        verify(userService, times(1)).hasSufficientBalance(senderID, 100.5);
    }

    @Test
    public void testSubmitTransaction_shouldThrowIllegalArgumentException_whenTargetEmailIsNotAPartner() {

        long senderUserId = 1;
        String partnerEmail = "email";
        String description = "description";
        double amount = 100;

        User receiver = new User();
        receiver.setEmail(partnerEmail);
        receiver.setUserId(2);
        receiver.setFirstName("John");
        receiver.setLastName("Lennon");

        User sender = new User();
        sender.setUserId(senderUserId);

        when(userService.findByEmail(partnerEmail)).thenReturn(receiver);

        when(partnershipService.isAPartner(anyLong(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.submitTransaction(senderUserId, partnerEmail, amount, description);
        });
    }
}
