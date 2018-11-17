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
        Message message = a.getMessage();
        if (!ackMap.containsKey(message))
            ackMap.put(message, 1);
        else {
            ackMap.replace(
                    message,
                    ackMap.get(message) + 1  );

            if (message.equals(messageQueue.get(0)) && ackMap.get(message) == numProc) {
                messageQueue.remove(message);
                ackMap.remove(message);
            }
        }
    }

    public void addMessage(Message m) {
        messageQueue.add(m);
        messageQueue.sort(Message::compareTo);
        ackMap.put(m, 0);
        sendAcknowledgement(m);
    }

    private void sendAcknowledgement(Message m) {
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
