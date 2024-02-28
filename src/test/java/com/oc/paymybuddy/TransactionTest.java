package com.oc.paymybuddy;

import com.oc.paymybuddy.model.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;


public class TransactionTest {

    @Test
    public void testEquals() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Transaction transaction = new Transaction();
        transaction.setAmount(1000);
        transaction.setDescription("Description");
        transaction.setTimestamp(timestamp);
        transaction.setSenderUserId(1);
        transaction.setReceiverUserId(2);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(1000);
        transaction2.setDescription("Description");
        transaction2.setTimestamp(timestamp);
        transaction2.setSenderUserId(1);
        transaction2.setReceiverUserId(2);

        assertEquals(transaction, transaction2);

    }

    @Test
    public void testHashCode() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Transaction transaction = new Transaction();
        transaction.setAmount(1000);
        transaction.setDescription("Description");
        transaction.setTimestamp(timestamp);
        transaction.setSenderUserId(1);
        transaction.setReceiverUserId(2);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(1000);
        transaction2.setDescription("Description");
        transaction2.setTimestamp(timestamp);
        transaction2.setSenderUserId(1);
        transaction2.setReceiverUserId(2);

        assertEquals(transaction.hashCode(), transaction2.hashCode());

    }

}
