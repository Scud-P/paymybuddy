package com.oc.paymybuddy;

import com.oc.paymybuddy.controller.UserController;
import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.PartnershipID;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

        User currentUser = new User();
        currentUser.setEmail("email");
        currentUser.setPassword("password");
        currentUser.setFirstName("Bob");
        currentUser.setLastName("Ross");

        HttpSession session = new MockHttpSession();
        session.setAttribute("user", currentUser);

        String firstname = "Bobette";
        String lastName = "Rossignol";
        String password = "changedPassword";
        String email = "changedEmail";

        when(userService.modifyUserInfo(currentUser)).thenReturn(currentUser);

        mockMvc.perform(post("/submitNewInfo")
                .param("firstName", firstname)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password))
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

        Partnership partnership = new Partnership();
        PartnershipID partnershipID = new PartnershipID();
        partnershipID.setSenderId(1);
        partnershipID.setReceiverId(2);
        partnership.setId(partnershipID);

        String email = "connectionEmail";

        User currentUser = new User();
        currentUser.setEmail("email");
        currentUser.setPassword("password");
        currentUser.setFirstName("Bob");
        currentUser.setLastName("Ross");

        HttpSession session = new MockHttpSession();
        session.setAttribute("user", currentUser);

        when(partnershipService.addPartnership(currentUser, email)).thenReturn(partnership);

        mockMvc.perform(post("/addConnection")
                        .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));
    }

}
