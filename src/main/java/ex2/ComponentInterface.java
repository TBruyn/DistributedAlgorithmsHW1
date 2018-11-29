package ex2;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
}
