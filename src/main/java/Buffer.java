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
        logger.info(String.format("reveive ack %s", a.toString()));
        Message message = a.getMessage();
        // lock object when processing acknowledgment
        synchronized (this) {
            // if first ack of msg create entry in ackmap
            if (!ackMap.containsKey(message))
                ackMap.put(message, 1);
                // else update number of acks received
            else {
                ackMap.replace(message, ackMap.get(message) + 1);
                if (ackMap.get(message) != numProc)
                    return;

                // if all acks for message at head of queue are received then deliver
                deliverMessages();
            }
        }
    }

    /**
     * check if head of message queue is fully ack'd
     * if so deliver it and repeat
     * if not stop
     */
    private void deliverMessages() {
        while (!messageQueue.isEmpty()) {
            Message head = messageQueue.get(0);
            if (ackMap.containsKey(head) && ackMap.get(head) == numProc) {
                logger.warn(String.format("deliver msg %s", head.toString()));
                messageQueue.remove(head);
                ackMap.remove(head);
                deliveredMessages.add(head);
            }
            else {
                break;
            }
        }
    }


    /**
     * add message to queue and resort to ensure correct order
     * @param m
     */
    public void addMessage(Message m) {
        logger.info(String.format("reveive msg %s", m.toString()));
        // lock object when adding message to queue
        synchronized (this) {
            messageQueue.add(m);
            messageQueue.sort(Message::compareTo);
        }
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
