package ex2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface ComponentInterface extends Remote {

    /**
     * contract for messages between components:
     * all normal messages have value >= 0
     * the special marker message has value -1
     */
    int MARKER = -1;

    /**
     * this method should put the message at the end of the
     * incoming channel of the receiver / outgoing channel of the sender
     * @param sender, message
     */
    void receive(int sender, int message) throws RemoteException;

    List<Map<Integer, int[]>> getStateHistory() throws RemoteException;

    List<Map<Integer, Queue<Integer>>> getChannelHistory() throws  RemoteException;
}
