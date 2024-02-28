package com.oc.paymybuddy;

import com.oc.paymybuddy.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testEquals() {

        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Ross");
        user.setEmail("test email");
        user.setBalance(1000);
        user.setUserId(1);
        user.setPassword("test password");

        User user2 = new User();
        user2.setFirstName("Bob");
        user2.setLastName("Ross");
        user2.setEmail("test email");
        user2.setBalance(1000);
        user2.setUserId(1);
        user2.setPassword("test password");

        assertEquals(user, user2);

    }

    @Test
    public void testHashCode() {

        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Ross");
        user.setEmail("test email");
        user.setBalance(1000);
        user.setUserId(1);
        user.setPassword("test password");

        User user2 = new User();
        user2.setFirstName("Bob");
        user2.setLastName("Ross");
        user2.setEmail("test email");
        user2.setBalance(1000);
        user2.setUserId(1);
        user2.setPassword("test password");

        assertEquals(user.hashCode(), user2.hashCode());

    }

}
