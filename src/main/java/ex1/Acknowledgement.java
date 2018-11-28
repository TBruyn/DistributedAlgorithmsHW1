package ex1;

import java.io.Serializable;

public class Acknowledgement implements Serializable {
    private final Message message;
    public Acknowledgement(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("Ack"+message.toString());
    }
}
