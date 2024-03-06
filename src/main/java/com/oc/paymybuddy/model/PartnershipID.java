package com.oc.paymybuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class PartnershipID implements Serializable {

    @Column(name = "sender_user_id")
    private long senderUserId;

    @Column(name = "receiver_user_id")
    private long receiverUserId;

    public long getSenderId() {
        return senderUserId;
    }

    public void setSenderId(long senderId) {
        this.senderUserId = senderId;
    }

    public long getReceiverId() {
        return receiverUserId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverUserId = receiverId;
    }



}
