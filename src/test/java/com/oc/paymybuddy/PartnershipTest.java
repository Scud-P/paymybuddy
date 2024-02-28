package com.oc.paymybuddy;

import com.oc.paymybuddy.model.Partnership;
import com.oc.paymybuddy.model.PartnershipID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PartnershipTest {

    @Test
    public void testEquals() {

        PartnershipID partnershipID = new PartnershipID();
        partnershipID.setSenderId(1);
        partnershipID.setReceiverId(2);

        Partnership partnership = new Partnership();
        partnership.setId(partnershipID);

        Partnership partnership2 = new Partnership();
        partnership2.setId(partnershipID);

        assertEquals(partnership, partnership2);

    }

    @Test
    public void testHashCode() {

        PartnershipID partnershipID = new PartnershipID();
        partnershipID.setSenderId(1);
        partnershipID.setReceiverId(2);

        Partnership partnership = new Partnership();
        partnership.setId(partnershipID);

        Partnership partnership2 = new Partnership();
        partnership2.setId(partnershipID);

        assertEquals(partnership.hashCode(), partnership2.hashCode());

    }

}
