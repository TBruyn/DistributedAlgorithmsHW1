import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BufferTest {

    @Test
    void testGet() {
        Buffer buffer = new Buffer(3);
        assertEquals(3, buffer.getNumberOfProcesses());
        assertTrue(buffer.getMessageQueue().isEmpty());
        assertTrue(buffer.getAcknowledgementMap().isEmpty());
    }

    @Test
    void testAddMessage() {
        Buffer buffer = new Buffer(3);

        Message first   = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        Message second  = new Message(1, new Timestamp(new int[]{2, 3, 4}));
        Message third   = new Message(2, new Timestamp(new int[]{2, 3, 4}));
        Message fourth  = new Message(1, new Timestamp(new int[]{3, 4, 5}));

        buffer.addMessage(fourth);
        buffer.addMessage(third);
        buffer.addMessage(first);
        buffer.addMessage(second);

        List<Message> messageQueue = buffer.getMessageQueue();

        assertEquals(first, messageQueue.get(0));
        assertEquals(second, messageQueue.get(1));
        assertEquals(third, messageQueue.get(2));
        assertEquals(fourth, messageQueue.get(3));
    }

    @Test
    void testAddAcknowledgementSimple() {
        Buffer buffer = new Buffer(3);

        Message message   = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        Acknowledgement acknowledgement = new Acknowledgement(message);

        buffer.addMessage(message);

        assertEquals(Integer.valueOf(0), buffer.getAcknowledgementMap().get
                (message));

        buffer.addAcknowledgement(acknowledgement);

        assertEquals(Integer.valueOf(1), buffer.getAcknowledgementMap().get
                (message));
    }

    @Test
    void testAddAcknowledgementMessageDoesNotExist() {
        Buffer buffer = new Buffer(3);

        Message message   = new Message(1, new Timestamp(new int[]{1, 2, 3}));
        Acknowledgement acknowledgement = new Acknowledgement(message);

        buffer.addAcknowledgement(acknowledgement);

        assertEquals(Integer.valueOf(1), buffer.getAcknowledgementMap().get
                (message));
    }
}
