import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

public class TimestampTest {

    @Test
    public void testGet() {
        int[] array = {2, 3, 4, 5};
        Timestamp stamp = new Timestamp(array);
        assertTrue(Arrays.equals(array, stamp.getTime()));
    }

    @Test
    public void testEquals() {
        Timestamp first = new Timestamp(new int[]{2, 3, 4, 5});
        Timestamp second = new Timestamp(new int[]{2, 3, 4, 5});

        assertTrue(first.equals(second));
    }

    @Test
    public void testCompareToEquals() {
        Timestamp bigger = new Timestamp(new int[]{2, 3, 4, 5});
        Timestamp smaller = new Timestamp(new int[]{5, 4, 3, 2});

        assertEquals(0, bigger.compareTo(smaller));
    }

    @Test
    public void testCompareToBigger() {
        Timestamp bigger = new Timestamp(new int[]{2, 3, 4, 5});
        Timestamp smaller = new Timestamp(new int[]{1, 2, 3, 4});

        assertEquals(1, bigger.compareTo(smaller));
        assertEquals(-1, smaller.compareTo(bigger));
    }
}
