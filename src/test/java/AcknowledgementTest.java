import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcknowledgementTest {

    @Test
    void testGet() {
        Message message   = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        Acknowledgement acknowledgement = new Acknowledgement(message);

        assertEquals(message, acknowledgement.getMessage());
    }

}
