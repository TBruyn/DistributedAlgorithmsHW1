import ex1.Acknowledgement;
import ex1.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcknowledgementTest {

    @Test
    void testGet() {
        Message message   = new Message(1,5);
        Acknowledgement acknowledgement = new Acknowledgement(message);

        assertEquals(message, acknowledgement.getMessage());
    }

}
