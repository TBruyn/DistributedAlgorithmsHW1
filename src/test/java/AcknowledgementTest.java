import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcknowledgementTest {

    @Test
    void testGet() {
        Timestamp stamp = new Timestamp(new int[]{1, 2, 3});
        int id = 3;
        Acknowledgement message = new Acknowledgement(id, stamp);
        assertEquals(id, message.getMessageSender());
        assertEquals(stamp, message.getTimestamp());
    }

    @Test
    void testEquals() {
        Acknowledgement first = new Acknowledgement(1, new Timestamp(new int[]{1, 2, 3}));
        Acknowledgement second = new Acknowledgement(1, new Timestamp(new int[]{1, 2, 3}));
        assertEquals(first, second);
    }
}
