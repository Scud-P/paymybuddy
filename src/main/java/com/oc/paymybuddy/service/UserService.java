package com.oc.paymybuddy.service;

import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserRepository userRepository;

    public User saveUserWithBasicInfo(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        logger.info("User {} found for email {}", user, email);
        return user;
    }

    public User saveUser(User user) {
        User userToAdd = new User();
        userToAdd.setFirstName(user.getFirstName());
        userToAdd.setLastName(user.getLastName());
        userToAdd.setEmail(user.getEmail());
        userToAdd.setPassword(user.getPassword());
        return userRepository.save(userToAdd);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }


    @Transactional
    public User modifyUserInfo(User modifiedUser) {
        User existingUser = userRepository.findById(modifiedUser.getUserId()).orElse(null);
        if (existingUser != null) {
            logger.info("User {} {} found in DB", existingUser.getFirstName(), existingUser.getLastName());
            if (modifiedUser.getFirstName() != null) {
                existingUser.setFirstName(modifiedUser.getFirstName());
            }
            if (modifiedUser.getLastName() != null) {
                existingUser.setLastName(modifiedUser.getLastName());
            }
            if (modifiedUser.getEmail() != null) {
                existingUser.setEmail(modifiedUser.getEmail());
            }
            if (modifiedUser.getPassword() != null) {
                existingUser.setPassword(modifiedUser.getPassword());
            }
            logger.info("Profile modified to {} {} {}", existingUser.getFirstName(), existingUser.getLastName(), existingUser.getEmail());
            userRepository.saveAndFlush(existingUser);
            return existingUser;
        } else {
            return null;
        }
    }

    @Transactional
    public User buyCredit(User user, double purchasedCredit) {
        User existingUser = userRepository.findById(user.getUserId()).orElse(null);
        if (existingUser != null) {
            logger.info("User {} {} found in DB", existingUser.getFirstName(), existingUser.getLastName());
            double currentBalance = user.getBalance();
            logger.info("Current balance for {}: {}", user.getUserId(), user.getBalance());
            double newBalance = currentBalance + purchasedCredit;
            user.setBalance(newBalance);
            userRepository.save(user);
            logger.info("Current balance for UserId[{}]: {}", user.getUserId(), user.getBalance());
            return user;
        } else {
            return null;
        }
    }

    public void setSenderBalance(HttpSession session, double amount) {
        User sender = (User) session.getAttribute("user");
        double currentBalance = sender.getBalance();
        double newBalance = currentBalance - amount;
        sender.setBalance(newBalance);
        logger.info("User with ID [{}] debited from {}", sender.getUserId(), amount);
        userRepository.save(sender);
    }

    public void setReceiverBalance(User receiver, double amount) {
        double currentBalance = receiver.getBalance();
        double newBalance = currentBalance + amount;
        receiver.setBalance(newBalance);
        logger.info("User with ID [{}] credited for {}", receiver.getUserId(), amount);
        userRepository.save(receiver);
    }

    public boolean hasSufficientBalance(HttpSession session, double amount) {
        User user = (User) session.getAttribute("user");
        double currentBalance = user.getBalance();
        return currentBalance >= amount;
    }

    public User findById(long receiverUserId) {
        return userRepository.findById(receiverUserId).orElse(null);
    }
}

