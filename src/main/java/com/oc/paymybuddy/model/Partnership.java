package com.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

    @Data
    @Entity
    @Table(name = "partnership")
    public class Partnership {
        public PartnershipID getId() {
            return id;
        }

        public void setId(PartnershipID id) {
            this.id = id;
        }

        @EmbeddedId
        private PartnershipID id;
    }

