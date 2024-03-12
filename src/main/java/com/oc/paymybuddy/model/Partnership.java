package com.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

    @Data
    @Entity
    @Table(name = "partnership")
    public class Partnership {

        public Partnership() {
        }

        public Partnership(long ownerId, long partnerId) {
            this.ownerId = ownerId;
            this.partnerId = partnerId;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "partnership_id")
        private long partnershipId;

        @Column(name = "owner_id")
        private long ownerId;

        @Column(name = "partner_id")
        private long partnerId;



    }

