import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class MessageTest {

    @Test
    void testGet() {
        Timestamp stamp = new Timestamp(new int[]{1, 2, 3});
        int id = 3;
        Message message = new Message(id, stamp);
        assertEquals(id, message.getSender());
        assertEquals(stamp, message.getTimestamp());
    }

    @Test
    void testEquals() {
        Message first = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        Message second = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        assertEquals(first, second);
    }

    @Test
    void testCompareToEquals() {
        Message first = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        Message second = new Message(1, new Timestamp(new int[]{3, 2, 1}));
        assertEquals(0, first.compareTo(second));
    }

    @Test
    void testCompareToUnequalStamp() {
        Message bigger = new Message(1, new Timestamp(new int[]{2, 3, 4}));
        Message smaller = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        assertEquals(1, bigger.compareTo(smaller));
        assertEquals(-1, smaller.compareTo(bigger));
    }
    @Test
    void testCompareToEqualStampUnequalId() {
        Message bigger = new Message(2, new Timestamp(new int[]{3, 2, 1}));
        Message smaller = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        assertEquals(1, bigger.compareTo(smaller));
        assertEquals(-1, smaller.compareTo(bigger));
    }
}
