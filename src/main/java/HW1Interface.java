import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HW1Interface extends Remote {
    void addMessage() throws RemoteException;
    void addAck() throws RemoteException;
}
