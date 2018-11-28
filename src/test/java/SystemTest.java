import ex1.HW1;
import ex1.HW1Interface;
import ex1.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class SystemTest {

    @Test
    void test() {
        int numberProcesses = 6;

        /** create the registry */
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numberProcesses; i++) {
            try {
                launchInThread(i, numberProcesses);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("some exception occurred when launching processes");
            }
        }

        //Let processes run
        try {
            Thread.sleep(120*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Collect results
        List<Message>[] deliveredMessages = retrieveDeliveredMessages(numberProcesses);

        int minLength = Integer.MAX_VALUE;
        for(int i = 0; i < numberProcesses; i++) {
            System.out.println(deliveredMessages[i].toString());
            if (deliveredMessages[i].size() < minLength) {
                minLength = deliveredMessages[i].size();
            }
        }

        System.out.println("Minimum length: " + minLength);

        List<Message> prevMessageList = deliveredMessages[0].subList(0, minLength);
        for (int i = 1; i < numberProcesses; i++) {
            List<Message> nextMessageList = deliveredMessages[i].subList(0, minLength);
            assertEquals(prevMessageList, nextMessageList);
            prevMessageList = nextMessageList;
        }
    }

    private List<Message>[] retrieveDeliveredMessages(int numberProcesses) {
        List<Message>[] messagesPerProcess = new List[numberProcesses];

        Registry registry;
        try {
            registry = LocateRegistry.getRegistry();

        } catch (RemoteException e) {
            e.printStackTrace();
            return messagesPerProcess;
        }

        for (int i = 0; i < numberProcesses; i++) {
            try {
                HW1Interface process = (HW1Interface) registry.lookup(String.valueOf(i));
                List<Message> deliveredMessages = process.deliveredMessages();
                messagesPerProcess[i] = deliveredMessages;
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }

        return messagesPerProcess;
    }

    private static void launchInThread(int pid, int n) {
        new Thread(() -> {
            try {
                HW1 process = new HW1(pid, n);
                HW1Interface stub = (HW1Interface) UnicastRemoteObject.exportObject(process, 0);
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(String.valueOf(pid), stub);
                System.out.println(String.format("Process %s bound", String.valueOf(pid)));
                process.startProcess();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
