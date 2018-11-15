import java.util.Objects;

public class Message implements Comparable{
    private final int sender;
    private final Timestamp timestamp;

    public Message(int sender, Timestamp timestamp) {
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public int getSender() {
        return sender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return sender == message.getSender() &&
                Objects.equals(timestamp, message.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, timestamp);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
