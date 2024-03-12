package com.oc.paymybuddy;

import com.oc.paymybuddy.model.DTO.TransactionDTO;
import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
