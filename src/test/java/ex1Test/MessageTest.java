package ex1Test;

import ex1.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class MessageTest {

    @Test
    void testGet() {
        int stamp = 5;
        int id = 3;
        Message message = new Message(id, stamp);

        assertEquals(id,    message.getSender());
        assertEquals(stamp, message.getTimestamp());
    }

    @Test
    void testEquals() {
        Message first   = new Message(1, 5);
        Message second  =  new Message(1, 5);

        assertEquals(first, second);
    }

    @Test
    void testCompareToEquals() {
        Message first   = new Message(1, 5);
        Message second  = new Message(1, 5);

        assertEquals(0, first.compareTo(second));
    }

    @Test
    void testCompareToUnequalStamp() {
        Message bigger  = new Message(1, 5);
        Message smaller = new Message(1, 2);

        assertEquals(1,     bigger.compareTo(smaller));
        assertEquals(-1,    smaller.compareTo(bigger));
    }
    @Test
    void testCompareToEqualStampUnequalId() {
        Message bigger  = new Message(2, 5);
        Message smaller = new Message(1, 5);

        assertEquals(1,     bigger.compareTo(smaller));
        assertEquals(-1,    smaller.compareTo(bigger));
    }
}
