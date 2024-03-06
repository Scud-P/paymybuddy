package com.oc.paymybuddy.service;

import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.UserRepository;
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

        if (!isValidUserInfo(firstName, lastName, email, password)) {
            return null;
        }

        if (!userRepository.isAlreadyUsedEmail(email)) {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            return userRepository.save(user);
        }
        logger.error("A user with the email address {} already exists email addresses must be unique", email);
        return null;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        logger.info("User {} found for email {}", user, email);
        return user;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }


    @Transactional
    public User modifyUserInfo(User modifiedUser) {

        try {
            User existingUser = userRepository.findById(modifiedUser.getUserId()).orElse(null);

            if (existingUser == null) {
                logger.info("User with ID {} not found in the database", modifiedUser.getUserId());
                return null;
            }

            if (modifiedUser.getEmail() != null && !modifiedUser.getEmail().equals(existingUser.getEmail())) {
                if (userRepository.isAlreadyUsedEmail(modifiedUser.getEmail())) {
                    logger.warn("A user with the email address {} already exists email addresses must be unique", modifiedUser);
                    throw new IllegalArgumentException("The email address you are trying to use already belongs to one of our users ("+modifiedUser.getEmail()+")");
                }
                existingUser.setEmail(modifiedUser.getEmail());
            }

            if (modifiedUser.getFirstName() != null && !modifiedUser.getFirstName().equals(existingUser.getFirstName())) {
                existingUser.setFirstName(modifiedUser.getFirstName());
            }
            if (modifiedUser.getLastName() != null && !modifiedUser.getFirstName().equals(existingUser.getFirstName())) {
                existingUser.setLastName(modifiedUser.getLastName());
            }
            if (modifiedUser.getPassword() != null && !modifiedUser.getPassword().equals(existingUser.getPassword())) {
                existingUser.setPassword(modifiedUser.getPassword());
            }
            logger.info("Profile modified to {} {} {}", existingUser.getFirstName(), existingUser.getLastName(), existingUser.getEmail());
            userRepository.saveAndFlush(existingUser);
            return existingUser;

        } catch (IllegalArgumentException e) {
            logger.error("An IllegalArgumentException occurred during transaction processing: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            logger.error("An error occurred during transaction processing: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public User buyCredit(User user, double purchasedCredit) {
        User existingUser = userRepository.findById(user.getUserId()).orElse(null);
        if (existingUser != null) {
            logger.info("User {} {} found in DB", existingUser.getFirstName(), existingUser.getLastName());
            double currentBalance = user.getBalance();
            logger.info("Current balance for UserId {}: {}", user.getUserId(), user.getBalance());
            double newBalance = currentBalance + purchasedCredit;
            user.setBalance(newBalance);
            userRepository.save(user);
            logger.info("Balance for UserId{} after purchase: {}", user.getUserId(), user.getBalance());
            return user;
        } else {
            return null;
        }
    }

    public void setSenderBalance(long senderUserId, double amount) {
        User sender = findById(senderUserId);
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

    public boolean hasSufficientBalance(long userId, double amount) {
        User user = findById(userId);
        double currentBalance = user.getBalance();
        return currentBalance >= amount;
    }

    public User findById(long receiverUserId) {
        return userRepository.findById(receiverUserId).orElse(null);
    }

    private boolean isValidUserInfo(String firstName, String lastName, String email, String password) {
        if (firstName == null || firstName.isEmpty()) {
            logger.warn("Field for first name can't be empty");
            return false;
        }

        if (lastName == null || lastName.isEmpty()) {
            logger.warn("Field for last name can't be empty");
            return false;
        }

        if (email == null || email.isEmpty()) {
            logger.warn("Field for email can't be empty");
            return false;
        }

        if (password == null || password.isEmpty()) {
            logger.warn("Field for password can't be empty");
            return false;
        }

        return true;
    }

}

