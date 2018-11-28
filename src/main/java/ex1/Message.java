package ex1;

import java.io.Serializable;
import java.util.Objects;

public class Message implements Comparable<Message>, Serializable {
    private final int sender;
    private final int timestamp;

    public Message(int sender, int timestamp) {
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public int getSender() {
        return sender;
    }

    public int getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return sender == message.getSender() &&
                timestamp == message.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, timestamp);
    }

    @Override
    public int compareTo(Message other) {
        if (this.timestamp < other.getTimestamp())
            return -1;
        else if (this.timestamp > other.getTimestamp())
            return 1;
        else {
            if (this.sender < other.getSender())
                return -1;
            else if (this.sender > other.getSender())
                return 1;
            else
                return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("(sender: %d, ts: %d)", sender, timestamp);
    }
}
