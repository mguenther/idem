package net.mguenther.idem;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestUtil {

    public static void printBinary(final long number) {
        for(int i = 0; i < Long.numberOfLeadingZeros(number); i++) {
            System.out.print('0');
        }
        System.out.println(Long.toBinaryString(number));
    }

    public static void assertThatListIsStrictlyOrdered(final List<Long> generatedIds) {
        Long left = generatedIds.get(0);
        for (int i = 1; i < generatedIds.size(); i++) {
            Long right = generatedIds.get(i);
            if (left > right) {
                fail("The list is not ordered. The offending items are '" + left + "' (index: " + (i-1) + ") and '" + right + "' (index: " + i + ").");
            } else if (left.equals(right)) {
                fail("The list contains duplicates ('" + left + "') at index " + (i-1) + " and " + i + ".");
            }
            assertTrue(left < right);
            left = right;
        }
    }
}
