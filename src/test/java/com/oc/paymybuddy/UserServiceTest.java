package com.oc.paymybuddy;

import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.UserRepository;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    private static User testUser;

    @BeforeEach
    public void createTestUser() {
        String firstName = "Bob";
        String lastName = "Ross";
        String email = "bobross@gmail.com";
        String password = "12345";
        double balance = 0;
        long userId = 0L;

        testUser = new User();
        testUser.setFirstName(firstName);
        testUser.setLastName(lastName);
        testUser.setEmail(email);
        testUser.setBalance(balance);
        testUser.setPassword(password);
        testUser.setUserId(userId);
    }

    @Test
    public void saveUserWithBasicInfoTest() {
        userService.saveUserWithBasicInfo(testUser.getFirstName(), testUser.getLastName(), testUser.getEmail(), testUser.getPassword());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void findByEmail() {
        String email = "bobross@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(testUser);
        User result = userService.findByEmail(email);

        assertEquals(result, testUser);
    }

    @Test
    public void modifyUserInfo() {

        String modifiedFirstName = "John";
        String modifiedLastName = "Lennon";
        String modifiedEmail = "johnlennon@gmail.com";
        String modifiedPassword = "23456";

        User modifiedBobRoss = new User();
        modifiedBobRoss.setFirstName(modifiedFirstName);
        modifiedBobRoss.setLastName(modifiedLastName);
        modifiedBobRoss.setEmail(modifiedEmail);
        modifiedBobRoss.setPassword(modifiedPassword);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        userService.modifyUserInfo(modifiedBobRoss);

        verify(userRepository, times(1)).saveAndFlush(testUser);
    }

    @Test
    public void buyCredit() {
        double purchasedCredit = 1000.50;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        userService.buyCredit(testUser, purchasedCredit);

        verify(userRepository, times(1)).save(testUser);
        assertEquals(1000.50, testUser.getBalance());
    }

    @Test
    public void setSenderBalance() {

        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        double initialBalance = testUser.getBalance();
        double amount = 100.0;
        double newBalance = initialBalance - amount;

        HttpSession session = new MockHttpSession();
        session.setAttribute("user", testUser);
        session.setAttribute("userId", testUser.getUserId());

        userService.setSenderBalance(testUser.getUserId(), amount);

        session.setAttribute("balance", newBalance);

        assertEquals(newBalance, testUser.getBalance(), 0.0);
        verify(userRepository, times(1)).save(testUser);
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

        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        testUser.setBalance(100.0);

        double amount = 50.0;

        boolean result = userService.hasSufficientBalance(testUser.getUserId(), amount);

        assertTrue(result);
    }

    @Test
    public void findById() {

        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        User result = userService.findById(testUser.getUserId());

        verify(userRepository, times(1)).findById(testUser.getUserId());
        assertEquals(result, testUser);
    }

}