package com.oc.paymybuddy;

import com.oc.paymybuddy.controller.TransactionController;
import com.oc.paymybuddy.model.DTO.TransactionDTO;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    public TransactionController transactionController;

    @MockBean
    private PartnershipService partnershipService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserService userService;

    @MockBean
    private MockDBService mockDBService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testTransfer() throws Exception {

        long senderUserId = 1L;

        User user = new User();
        user.setUserId(senderUserId);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        List<String> emails = List.of("email1@example.com", "email2@example.com");
        when(partnershipService.getEmailsFromPartners(anyLong())).thenReturn(emails);

        List<TransactionDTO> transactionList = new ArrayList<>();
        Page<TransactionDTO> transactionPage = new PageImpl<>(transactionList);
        when(transactionService.getRecentTransactionsDTOBySenderUserID(anyLong(), any(Pageable.class))).thenReturn(transactionPage);

        mockMvc.perform(get("/transfer").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("emails"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    public void testTransferNoSession() throws Exception {

        mockMvc.perform(get("/transfer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

    }

    @Test
    public void testSubmitTransaction() throws Exception {

        String email = "email";
        double amount = 10.52;
        String description = "description";

        long senderUserId = 1L;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        User sender = new User();
        sender.setUserId(senderUserId);

        User receiver = new User();
        receiver.setUserId(2);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", sender);
        session.setAttribute("userId", sender.getUserId());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTimestamp(timestamp);
        transaction.setSenderUserId(sender.getUserId());
        transaction.setReceiverUserId(receiver.getUserId());
        transaction.setTransactionNumber(1);

        System.out.println(transaction);

        when(transactionService.submitTransaction(senderUserId, email, amount, description)).thenReturn(transaction);

        mockMvc.perform(post("/submitTransaction").session(session)
                .param("selectedEmail", email)
                .param("amount", String.valueOf(amount))
                .param("description", description))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));

        verify(transactionService, times(1)).submitTransaction(senderUserId, email, amount, description);

    }
}
