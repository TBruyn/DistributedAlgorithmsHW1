import java.util.HashMap;
import java.util.List;

public class Buffer {
    private List<Message> messageQueue;
    private HashMap<Message,Integer> ackMap;
    private int numProc;

    public Buffer(int numProcesses) {}

    public void addAck(Acknowledgement a) {

    }

    public void addMsg(Message m) {

    }

}
