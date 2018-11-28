package ex1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HW1Interface extends Remote {
    /**
     * called remotely from other process to send a message
     * @param m
     * @throws RemoteException
     */
    void addMessage(Message m) throws RemoteException;

    /**
     * called remotely from other process to send acknowledgement
     * @param a
     * @throws RemoteException
     */
    void addAck(Acknowledgement a) throws RemoteException;

    List<Message> deliveredMessages() throws RemoteException;
}
