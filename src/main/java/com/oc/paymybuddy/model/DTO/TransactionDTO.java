package com.oc.paymybuddy.model.DTO;

import com.oc.paymybuddy.model.Transaction;
import com.oc.paymybuddy.model.User;
import lombok.Data;

@Data
public class TransactionDTO {

    private String connectionFirstName;
    private String description;
    private String formattedAmount;

    public TransactionDTO(Transaction transaction, User receiver) {
        this.connectionFirstName = receiver.getFirstName();
        this.formattedAmount = transaction.getFormattedAmount() + "$";
        if (transaction.getDescription() != null) {
            this.description = transaction.getDescription();
        } else {
            this.description = "";
        }
    }
}
