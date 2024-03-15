package com.oc.paymybuddy;

import com.oc.paymybuddy.controller.AuthenticationController;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.UserRepository;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.UserService;
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


@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MockDBService mockDBService;

    @Test
    public void testLogin() throws Exception {

        String firstName = "Bob";
        String lastName = "Ross";
        String email = "bobross@gmail.com";
        String password = "12345";
        double balance = 0;
        long userId = 0L;

        User testUser = new User();
        testUser.setFirstName(firstName);
        testUser.setLastName(lastName);
        testUser.setEmail(email);
        testUser.setBalance(balance);
        testUser.setPassword(password);
        testUser.setUserId(userId);

        when(userService.findByEmail(email)).thenReturn(testUser);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", testUser);


        mockMvc.perform(post("/login")
                .param("email", "bobross@gmail.com")
                .param("password", "12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void testLoginUserDoesNotExist() throws Exception {

        String email = "bobross@gmail.com";

        when(userService.findByEmail(email)).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("email", "bobross@gmail.com")
                        .param("password", "12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

}
