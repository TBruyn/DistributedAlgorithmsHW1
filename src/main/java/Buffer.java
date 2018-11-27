import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Buffer {

    private List<Message> messageQueue;
    private HashMap<Message,Integer> ackMap;
    private int numProc;
    private int ownerPid;
    private Logger logger;
    private List<Message> deliveredMessages;

    public Buffer(int pid, int numProcesses) {
        ownerPid = pid;
        this.numProc    = numProcesses;
        messageQueue    = new ArrayList<>();
        ackMap          = new HashMap<>();
        deliveredMessages = new ArrayList<>();
        logger = LogManager.getLogger(String.format("P%d", pid));
    }

    /**
     * add ack of message m and deliver head of message queue
     * for all fully ack'd messages
     * @param a
     */
    public void addAcknowledgement(Acknowledgement a) {
        Message message = a.getMessage();
        // if first ack of msg create entry in ackmap
        if (!ackMap.containsKey(message))
            ackMap.put(message, 1);
        // else update number of acks received
        else {
            ackMap.replace(message, ackMap.get(message) + 1);
            if (ackMap.get(message) != numProc)
                return;

            // if all acks for message at head of queue are received then deliver
            while (!messageQueue.isEmpty()) {
                Message head = messageQueue.get(0);
                if (ackMap.containsKey(head) && ackMap.get(head) == numProc) {
                    logger.info(String.format("deliver msg %s", head.toString()));
                    messageQueue.remove(head);
                    ackMap.remove(head);
                    deliveredMessages.add(head);
                }
                else {
                    break;
                }
            }
        }
    }

    /**
     * add message to queue and resort to ensure correct order
     * @param m
     */
    public void addMessage(Message m) {
        logger.info(String.format("reveive msg %s", m.toString()));
        messageQueue.add(m);
        messageQueue.sort(Message::compareTo);
//        ackMap.put(m, 0);
        sendAcknowledgement(m);
    }

    /**
     * broadcast ack for message m to all processes (including owner process)
     * @param m
     */
    private void sendAcknowledgement(Message m) {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry();

        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < numProc; i++) {
            try {
                // lookup process by its id and send ack
                HW1Interface process = (HW1Interface) registry.lookup(String.valueOf(i));
//                System.out.println(String.format("p%d sending msg to p%d", ownerPid, i));
                process.addAck(new Acknowledgement(m));
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
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

    public List<Message> getDeliveredMessages() {
        return deliveredMessages;
    }
}
