package com.oc.paymybuddy;

import com.oc.paymybuddy.model.DTO.TransactionDTO;
import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import com.oc.paymybuddy.repository.UserRepository;
import com.oc.paymybuddy.service.PartnershipService;
import com.oc.paymybuddy.service.TransactionService;
import com.oc.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDTOTest {

    @Test
    public void testDefaultConstructorGettersAndSetters() {

        Transaction transaction = new Transaction();
        transaction.setAmount(1000);
        transaction.setDescription("Description");

        User receiver = new User();
        receiver.setFirstName("Bob");

        TransactionDTO transactionDTO = new TransactionDTO(transaction, receiver);

        assertEquals(transactionDTO.getConnectionFirstName(), receiver.getFirstName());
        assertEquals(transactionDTO.getFormattedAmount(), transaction.getFormattedAmount());
        assertEquals(transactionDTO.getDescription(), transaction.getDescription());

    }

    @Test
    public void testEquals() {
        Transaction transaction = new Transaction();
        transaction.setAmount(1000);
        transaction.setDescription("Description");

        User receiver = new User();
        receiver.setFirstName("Bob");

        TransactionDTO transactionDTO = new TransactionDTO(transaction, receiver);
        TransactionDTO transactionDTO2 = new TransactionDTO(transaction, receiver);

        assertEquals(transactionDTO, transactionDTO2);
    }

    @Test
    public void testHashCode() {
        Transaction transaction = new Transaction();
        transaction.setAmount(1000);
        transaction.setDescription("Description");

        User receiver = new User();
        receiver.setFirstName("Bob");

        TransactionDTO transactionDTO = new TransactionDTO(transaction, receiver);
        TransactionDTO transactionDTO2 = new TransactionDTO(transaction, receiver);

        assertEquals(transactionDTO.hashCode(), transactionDTO2.hashCode());
    }

    @SpringBootTest
    public static class TransactionIT {

        @Autowired
        private TransactionService transactionService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PartnershipService partnershipService;

        @Autowired
        private UserService userService;

        @Test
        @Transactional
        public void testSubmittingAValidTransaction() {

            double transactionAmount = 50.0;

            User sender = userService.findByEmail("john.smith@example.com");
            User receiver = userService.findByEmail("emily.johnson@example.com");
            double initialReceiverBalance = receiver.getBalance();

            userService.buyCredit(sender, 100.0);

            transactionService.submitTransaction(sender.getUserId(), receiver.getEmail(), transactionAmount, "poker");

            Optional<Transaction> savedTransaction = transactionService.findTransactionById(21L);
            assertTrue(savedTransaction.isPresent());

            Transaction transaction = savedTransaction.get();

            assertEquals(sender.getUserId(), transaction.getSenderUserId());
            assertEquals(receiver.getUserId(), transaction.getReceiverUserId());
            assertEquals(50.0, transaction.getAmount());
            assertEquals("poker", transaction.getDescription());

            User updatedSender = userRepository.findById(sender.getUserId()).orElse(null);
            assertNotNull(updatedSender);
            assertEquals(49.75, updatedSender.getBalance());

            User updatedReceiver = userRepository.findByEmail(receiver.getEmail());
            assertNotNull(updatedReceiver);
            assertEquals(initialReceiverBalance + transactionAmount, updatedReceiver.getBalance());
        }

        @Test
        @Transactional
        public void testSubmitTransactionNotAPartner() {

            double transactionAmount = 50.0;

            String email = "notapartner@example.com";
            String firstName = "Obiwan";
            String lastName = "Kenobi";
            String password = "12345";

            userService.saveUserWithBasicInfo(firstName, lastName, email, password);

            User sender = userService.findByEmail("john.smith@example.com");
            User receiver = userService.findByEmail("notapartner@example.com");

            double initialReceiverBalance = receiver.getBalance();

            userService.buyCredit(sender, 100.0);

            assertThrows(IllegalArgumentException.class, () -> {
                transactionService.submitTransaction(sender.getUserId(), receiver.getEmail(), transactionAmount, "poker");

            });

            Optional<Transaction> savedTransaction = transactionService.findTransactionById(21L);
            assertFalse(savedTransaction.isPresent());

            User updatedSender = userRepository.findById(sender.getUserId()).orElse(null);
            assertNotNull(updatedSender);
            assertEquals(sender.getBalance(), updatedSender.getBalance());

            User updatedReceiver = userRepository.findByEmail(receiver.getEmail());
            assertNotNull(updatedReceiver);
            assertEquals(initialReceiverBalance, updatedReceiver.getBalance());
        }

        @Test
        @Transactional
        public void testSubmitTransactionInsufficientFunds() {

            double transactionAmount = 110.0;

            User sender = userService.findByEmail("john.smith@example.com");
            User receiver = userService.findByEmail("emily.johnson@example.com");
            double initialReceiverBalance = receiver.getBalance();

            userService.buyCredit(sender, 100.0);

            assertThrows(IllegalArgumentException.class, () -> {
                transactionService.submitTransaction(sender.getUserId(), receiver.getEmail(), transactionAmount, "poker");

            });

            Optional<Transaction> savedTransaction = transactionService.findTransactionById(21L);
            assertFalse(savedTransaction.isPresent());

            User updatedSender = userRepository.findById(sender.getUserId()).orElse(null);
            assertNotNull(updatedSender);
            assertEquals(sender.getBalance(), updatedSender.getBalance());

            User updatedReceiver = userRepository.findByEmail(receiver.getEmail());
            assertNotNull(updatedReceiver);
            assertEquals(initialReceiverBalance, updatedReceiver.getBalance());
        }

        @Test
        @Transactional
        public void testAddPartnershipSuccessfullyFoundPartner() {

            String ownerEmail = "qui-gon@example.com";
            String ownerFirstname = "Qui-Gon";
            String ownerLastName = "Jinn";
            String ownerPassword = "12345";

            String partnerEmail = "obiwan@example.com";
            String partnerFirstName = "Obiwan";
            String partnerLastName = "Kenobi";
            String partnerPassword = "12345";

            userService.saveUserWithBasicInfo(ownerFirstname, ownerLastName, ownerEmail, ownerPassword);
            userService.saveUserWithBasicInfo(partnerFirstName, partnerLastName, partnerEmail, partnerPassword);

            User owner = userService.findByEmail(ownerEmail);
            long ownerUserId = owner.getUserId();

            User partner = userService.findByEmail(partnerEmail);
            long partnerUserId = partner.getUserId();

            partnershipService.addPartnership(ownerUserId, partnerEmail);

            Optional<Partnership> savedPartnership = partnershipService.getById(31L);
            assertTrue(savedPartnership.isPresent());

            Partnership partnership = savedPartnership.get();

            assertEquals(owner.getUserId(), partnership.getOwnerId());
            assertEquals(partnerUserId, partnership.getPartnerId());
        }

        @Test
        @Transactional
        public void testAddPartnershipNotFoundPartner() {

            String ownerEmail = "qui-gon@example.com";
            String ownerFirstname = "Qui-Gon";
            String ownerLastName = "Jinn";
            String ownerPassword = "12345";

            userService.saveUserWithBasicInfo(ownerFirstname, ownerLastName, ownerEmail, ownerPassword);

            User owner = userService.findByEmail(ownerEmail);
            long ownerUserId = owner.getUserId();

            String partnerEmail = "obiwan@example.com";

            assertThrows(IllegalArgumentException.class, () -> {
                partnershipService.addPartnership(ownerUserId, partnerEmail);

            });

            Optional<Partnership> savedPartnership = partnershipService.getById(31L);
            assertFalse(savedPartnership.isPresent());

            List<String> partnerEmails = partnershipService.getEmailsFromPartners(owner.getUserId());

            assertEquals(0, partnerEmails.size());
        }
    }
}
