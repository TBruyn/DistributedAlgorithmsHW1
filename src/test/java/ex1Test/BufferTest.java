package ex1Test;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BufferTest {

//    @Test
//    void testGet() {
//        ex1.Buffer buffer = new ex1.Buffer(3);
//
//        assertEquals(3, buffer.getNumberOfProcesses());
//        assertTrue(buffer.getMessageQueue().isEmpty());
//        assertTrue(buffer.getAcknowledgementMap().isEmpty());
//    }
//
//    @Test
//    void testAddMessage() {
//        ex1.Buffer buffer = new ex1.Buffer(3);
//
//        ex1.Message message = new ex1.Message(1, 5);
//
//        buffer.addMessage(message);
//
//        assertEquals(message, buffer.getMessageQueue().get(0));
//        assertTrue(buffer.getAcknowledgementMap().containsKey(message));
//    }
//
//    @Test
//    void testAddMessageSort() {
//        ex1.Buffer buffer = new ex1.Buffer(3);
//
//        ex1.Message first   = new ex1.Message(1, 1);
//        ex1.Message second  = new ex1.Message(1, 3);
//        ex1.Message third   = new ex1.Message(2, 3);
//        ex1.Message fourth  = new ex1.Message(1, 4);
//
//        buffer.addMessage(fourth);
//        buffer.addMessage(third);
//        buffer.addMessage(first);
//        buffer.addMessage(second);
//
//        List<ex1.Message> messageQueue = buffer.getMessageQueue();
//
//        assertEquals(first,     messageQueue.get(0));
//        assertEquals(second,    messageQueue.get(1));
//        assertEquals(third,     messageQueue.get(2));
//        assertEquals(fourth,    messageQueue.get(3));
//    }
//
//    @Test
//    void testAddAcknowledgementSimple() {
//        ex1.Buffer buffer = new ex1.Buffer(3);
//
//        ex1.Message message = new ex1.Message(1, 5);
//        ex1.Acknowledgement acknowledgement = new ex1.Acknowledgement(message);
//
//        buffer.addMessage(message);
//
//        assertEquals(Integer.valueOf(0), buffer.getAcknowledgementMap().get
//                (message));
//
//        buffer.addAcknowledgement(acknowledgement);
//
//        assertEquals(Integer.valueOf(1), buffer.getAcknowledgementMap().get
//                (message));
//    }
//
//    @Test
//    void testAddAcknowledgementMessageDoesNotExist() {
//        ex1.Buffer buffer = new ex1.Buffer(3);
//
//        ex1.Message message = new ex1.Message(1, 5);
//        ex1.Acknowledgement acknowledgement = new ex1.Acknowledgement(message);
//
//        buffer.addAcknowledgement(acknowledgement);
//
//        assertEquals(Integer.valueOf(1), buffer.getAcknowledgementMap().get
//                (message));
//    }
//
//    @Test
//    void testAddAcknowledgementTwice() {
//        ex1.Buffer buffer = new ex1.Buffer(3);
//
//        ex1.Message message = new ex1.Message(1, 5);
//        ex1.Acknowledgement acknowledgement = new ex1.Acknowledgement(message);
//
//        buffer.addMessage(message);
//
//        assertEquals(Integer.valueOf(0), buffer.getAcknowledgementMap().get
//                (message));
//
//        buffer.addAcknowledgement(acknowledgement);
//        buffer.addAcknowledgement(acknowledgement);
//
//        assertEquals(Integer.valueOf(2), buffer.getAcknowledgementMap().get
//                (message));
//    }
//
//    @Test
//    void testAddAcknowledgementLast() {
//        ex1.Buffer buffer = new ex1.Buffer(3);
//
//        ex1.Message message = new ex1.Message(1, 5);
//        ex1.Acknowledgement acknowledgement = new ex1.Acknowledgement(message);
//
//        buffer.addMessage(message);
//
//        assertEquals(Integer.valueOf(0), buffer.getAcknowledgementMap().get
//                (message));
//
//        buffer.addAcknowledgement(acknowledgement);
//        buffer.addAcknowledgement(acknowledgement);
//        buffer.addAcknowledgement(acknowledgement);
//
//        assertFalse(buffer.getAcknowledgementMap().containsKey(message));
//        assertFalse(buffer.getMessageQueue().contains(message));
//    }
}
