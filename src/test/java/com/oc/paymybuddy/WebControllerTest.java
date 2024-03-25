package com.oc.paymybuddy;


import com.oc.paymybuddy.controller.WebController;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.service.MockDBService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebController.class)


public class WebControllerTest {

    @MockBean
    public MockDBService mockDBService;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public WebController webController;

    @Test
    public void testGetSignup() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetIndex() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetConnections() throws Exception {
        mockMvc.perform(get("/connections"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetContact() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetModifyInfo() throws Exception {
        mockMvc.perform(get("/modifyInfo"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBuyCredit() throws Exception {
        User user = new User();
        user.setBalance(0);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);
        mockMvc.perform(get("/buyCredit").session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }


}
