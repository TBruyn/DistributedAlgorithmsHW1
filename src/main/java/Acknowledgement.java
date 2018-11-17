import java.util.Objects;

public class Acknowledgement {
    private int messageSender;
    private Timestamp timestamp;

    public Acknowledgement(int messageSender, Timestamp timestamp) {
        this.messageSender = messageSender;
        this.timestamp = timestamp;
    }

    public int getMessageSender() {
        return messageSender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acknowledgement acknowledgement = (Acknowledgement) o;
        return messageSender == acknowledgement.getMessageSender() &&
                Objects.equals(timestamp, acknowledgement.getTimestamp());
    }
}
