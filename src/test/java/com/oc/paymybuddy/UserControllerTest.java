package com.oc.paymybuddy;

import com.oc.paymybuddy.controller.UserController;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

}
