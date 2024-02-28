package com.oc.paymybuddy;

import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.UserRepository;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@WebMvcTest(UserService.class)
public class UserServiceTest {

    @Autowired
    public UserService userService;

    @MockBean
    public UserRepository userRepository;

    @MockBean
    public MockDBService mockDBService;

    @Test
    public void saveUserWithBasicInfoTest() {

        String firstName = "Bob";
        String lastName = "Ross";
        String email = "bobross@gmail.com";
        String password = "12345";

        User bobRoss = new User();
        bobRoss.setFirstName(firstName);
        bobRoss.setLastName(lastName);
        bobRoss.setEmail(email);
        bobRoss.setPassword(password);

        userService.saveUserWithBasicInfo(firstName, lastName, email, password);

        verify(userRepository, times(1)).save(bobRoss);
    }

    @Test
    public void findByEmail() {

        String firstName = "Bob";
        String lastName = "Ross";
        String email = "bobross@gmail.com";
        String password = "12345";

        User bobRoss = new User();
        bobRoss.setFirstName(firstName);
        bobRoss.setLastName(lastName);
        bobRoss.setEmail(email);
        bobRoss.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(bobRoss);

        User result = userService.findByEmail(email);

        assertEquals(result, bobRoss);
    }

    @Test
    public void modifyUserInfo() {

        String firstName = "Bob";
        String lastName = "Ross";
        String email = "bobross@gmail.com";
        String password = "12345";

        User bobRoss = new User();
        bobRoss.setFirstName(firstName);
        bobRoss.setLastName(lastName);
        bobRoss.setEmail(email);
        bobRoss.setPassword(password);

        String modifiedFirstName = "John";
        String modifiedLastName = "Lennon";
        String modifiedEmail = "johnlennon@gmail.com";
        String modifiedPassword = "23456";

        User modifiedBobRoss = new User();
        modifiedBobRoss.setFirstName(modifiedFirstName);
        modifiedBobRoss.setLastName(modifiedLastName);
        modifiedBobRoss.setEmail(modifiedEmail);
        modifiedBobRoss.setPassword(modifiedPassword);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(bobRoss));

        userService.modifyUserInfo(modifiedBobRoss);

        verify(userRepository, times(1)).saveAndFlush(bobRoss);

    }

    @Test
    public void buyCredit() {

        String firstName = "Bob";
        String lastName = "Ross";
        String email = "bobross@gmail.com";
        String password = "12345";

        User bobRoss = new User();
        bobRoss.setFirstName(firstName);
        bobRoss.setLastName(lastName);
        bobRoss.setEmail(email);
        bobRoss.setPassword(password);
        bobRoss.setBalance(0);

        double purchasedCredit = 1000.50;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(bobRoss));

        userService.buyCredit(bobRoss, purchasedCredit);

        verify(userRepository, times(1)).save(bobRoss);

        assertEquals(1000.50, bobRoss.getBalance());
    }

    @Test
    public void setSenderBalance() {

        double initialBalance = 1000.0;
        double amount = 100.0;
        double newBalance = initialBalance - amount;

        User sender = new User();
        sender.setBalance(initialBalance);

        HttpSession session = new MockHttpSession();
        session.setAttribute("user", sender);

        userService.setSenderBalance(session, amount);

        session.setAttribute("balance", newBalance);

        assertEquals(newBalance, sender.getBalance(), 0.0);
        verify(userRepository, times(1)).save(sender);
        assertEquals(newBalance, session.getAttribute("balance"));
    }

    @Test
    public void setReceiverBalance() {

        double initialBalance = 1000.0;
        double amount = 100.0;
        double newBalance = 1100.0;

        User receiver = new User();
        receiver.setBalance(initialBalance);

        userService.setReceiverBalance(receiver, amount);

        verify(userRepository, times(1)).save(receiver);
        assertEquals(newBalance, receiver.getBalance(), 0.0);
    }

    @Test
    public void hasSufficientBalance() {

        double amount = 500.0;

        User bobRoss = new User();
        bobRoss.setBalance(1000.0);

        HttpSession session = new MockHttpSession();
        session.setAttribute("user", bobRoss);

        boolean result = userService.hasSufficientBalance(session, amount);

        assertTrue(result);
    }

    @Test
    public void findById() {

        long bobId = 1;

        User bobRoss = new User();
        bobRoss.setUserId(bobId);

        when(userRepository.findById(bobId)).thenReturn(Optional.of(bobRoss));

        User result = userService.findById(bobId);

        verify(userRepository, times(1)).findById(bobId);
        assertEquals(result, bobRoss);

    }

}
