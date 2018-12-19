package ex3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ComponentInterface extends Remote {

    void callOrdinary(Message msg) throws RemoteException;

    void callCandidate(Message msg) throws RemoteException;

    void startCandidate() throws RemoteException;

    void terminate() throws RemoteException;
}
