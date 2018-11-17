import java.util.Objects;

public class Acknowledgement {
    private final Message message;
    public Acknowledgement(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
