package com.oc.paymybuddy;

import com.oc.paymybuddy.controller.UserController;
import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.User;
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

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private PartnershipService partnershipService;

    @MockBean
    private MockDBService mockDBService;

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddUser() throws Exception {

        String firstname = "Bob";
        String lastName = "Ross";
        String password = "password";
        String email = "email";

        User user = new User();
        user.setUserId(1);
        user.setEmail("email");
        user.setPassword("password");
        user.setBalance(0);
        user.setFirstName("Bob");
        user.setLastName("Ross");

        when(userService.saveUserWithBasicInfo(firstname, lastName, email, password)).thenReturn(user);

        mockMvc.perform(post("/addUser")
                .param("firstName", firstname)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testModifyUserInfo() throws Exception {

        User currentUser = mock(User.class);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", currentUser);

        String firstName = "Bobette";
        String lastName = "Rossignol";
        String password = "changedPassword";
        String email = "changedEmail";

        User modifiedUser = new User();
        modifiedUser.setFirstName(firstName);
        modifiedUser.setLastName(lastName);
        modifiedUser.setEmail(email);
        modifiedUser.setPassword(password);

        when(userService.modifyUserInfo(currentUser)).thenReturn(modifiedUser);
        session.setAttribute("user", modifiedUser);

        mockMvc.perform(post("/submitNewInfo").session(session)
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void testModifyUserInfo_Setters() {

        MockHttpSession session = new MockHttpSession();

        User currentUser = new User();
        currentUser.setFirstName("Bob");
        currentUser.setLastName("Ross");
        currentUser.setEmail("bob@ross.com");
        currentUser.setPassword("12345");

        session.setAttribute("user", currentUser);

        String firstName = "Bobette";
        String lastName = "Rossignol";
        String email = "bobette@rossignol.com";
        String password = "23456";

        String result = userController.modifyUserInfo(firstName, lastName, email, password, session, null);

        assertEquals("redirect:/profile", result);
        assertEquals(firstName, currentUser.getFirstName());
        assertEquals(lastName, currentUser.getLastName());
        assertEquals(email, currentUser.getEmail());
        assertEquals(password, currentUser.getPassword());
        verify(userService, times(1)).modifyUserInfo(currentUser);

    }

    @Test
    public void testModifyUserInfoCurrentUserNull() throws Exception {

        mockMvc.perform(post("/submitNewInfo")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "newEmail@example.com")
                        .param("password", "newPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }


    @Test
    public void testFinalizePurchase() throws Exception {

        double purchase = 100.50;

        User currentUser = new User();
        currentUser.setEmail("email");
        currentUser.setPassword("password");
        currentUser.setFirstName("Bob");
        currentUser.setLastName("Ross");

        HttpSession session = new MockHttpSession();
        session.setAttribute("user", currentUser);

        when(userService.buyCredit(currentUser, purchase)).thenReturn(currentUser);

        mockMvc.perform(post("/finalizePurchase")
                .param("purchase", String.valueOf(purchase)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void testAddConnection() throws Exception {

        long senderUserId = 1L;
        long receiverUserId = 2L;

        Partnership partnership = new Partnership();
        partnership.setOwnerId(senderUserId);
        partnership.setPartnerId(receiverUserId);

        String email = "connectionEmail";

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", senderUserId);

        when(partnershipService.addPartnership(2L, email)).thenReturn(partnership);


        mockMvc.perform(post("/addConnection").session(session)
                        .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));
    }

}
