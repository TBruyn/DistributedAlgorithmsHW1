import java.io.Serializable;
import java.util.Objects;

public class Acknowledgement implements Serializable {
    private final Message message;
    public Acknowledgement(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
