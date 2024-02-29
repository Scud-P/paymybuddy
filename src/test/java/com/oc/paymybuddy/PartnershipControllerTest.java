package com.oc.paymybuddy;

import com.oc.paymybuddy.controller.PartnershipController;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(PartnershipController.class)

public class PartnershipControllerTest {

    @Autowired
    public PartnershipController partnershipController;

    @MockBean
    public MockDBService mockDBService;

    @MockBean
    public UserService userService;

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public PartnershipService partnershipService;

    @Test
    public void testGetPartnerships() {

        Long userId = 1L;
        List<String> expectedEmails = List.of("email1@example.com", "email2@example.com");

        HttpSession session = new MockHttpSession();
        session.setAttribute("userId", userId);

        when(partnershipService.getEmailsFromPartners(userId)).thenReturn(expectedEmails);

        String result = partnershipController.getPartnerships(session);

        assertEquals("emails", result);
        verify(partnershipService).getEmailsFromPartners(userId);
    }
}
