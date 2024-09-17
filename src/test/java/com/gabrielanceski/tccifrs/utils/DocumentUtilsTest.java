package com.gabrielanceski.tccifrs.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DocumentUtilsTest {

    @Test
    public void testCheckCNPJ() {
        String invalidCNPJ = "12345678901234";
        String invalidCNPJ_2 = "11111111111111";
        String validCNPJ = "10637926000146";

        Assertions.assertFalse(DocumentUtils.checkCNPJ(invalidCNPJ));
        Assertions.assertFalse(DocumentUtils.checkCNPJ(invalidCNPJ_2));
        Assertions.assertTrue(DocumentUtils.checkCNPJ(validCNPJ));
    }

}
