import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Buffer {
    private List<Message> messageQueue;
    private HashMap<Message,Integer> ackMap;
    private int numProc;

    public Buffer(int numProcesses) {
        this.numProc = numProcesses;
        messageQueue = new ArrayList<>();
        ackMap = new HashMap<>();
    }

    public void addAcknowledgement(Acknowledgement a) {

    }

    public void addMessage(Message m) {
        messageQueue.add(m);
        messageQueue.sort(Message::compareTo);
    }

    public List<Message> getMessageQueue() {
        return messageQueue;
    }

    public HashMap<Message, Integer> getAcknowledgementMap() {
        return ackMap;
    }

    public int getNumberOfProcesses() {
        return numProc;
    }
}
