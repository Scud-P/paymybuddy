package com.oc.paymybuddy;

import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.UserRepository;
import com.oc.paymybuddy.service.MockDBService;
import com.oc.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@WebMvcTest(UserService.class)
public class UserServiceTest {

    @Autowired
    public UserService userservice;

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

        userservice.saveUserWithBasicInfo(firstName, lastName, email, password);

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

        User result = userservice.findByEmail(email);

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

        userservice.modifyUserInfo(modifiedBobRoss);

        verify(userRepository, times(1)).saveAndFlush(bobRoss);

    }

}
