package com.oc.paymybuddy;

import com.oc.paymybuddy.model.Partnership;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PartnershipTest {

    @Test
    public void testEquals() {

        Partnership partnership = new Partnership();
        partnership.setOwnerId(1);
        partnership.setPartnerId(2);
        partnership.setPartnershipId(1);

        Partnership partnership2 = new Partnership();
        partnership.setOwnerId(1);
        partnership.setPartnerId(2);
        partnership.setPartnershipId(1);

        assertEquals(partnership, partnership2);

    }

    @Test
    public void testHashCode() {

        Partnership partnership = new Partnership();
        partnership.setOwnerId(1);
        partnership.setPartnerId(2);
        partnership.setPartnershipId(1);

        Partnership partnership2 = new Partnership();
        partnership.setOwnerId(1);
        partnership.setPartnerId(2);
        partnership.setPartnershipId(1);

        assertEquals(partnership, partnership2);

        assertEquals(partnership.hashCode(), partnership2.hashCode());
    }
}
