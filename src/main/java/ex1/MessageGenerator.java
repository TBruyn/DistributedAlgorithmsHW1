package ex1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class MessageGenerator implements Runnable {
    private int numProcesses;
    private int ownerPid;
    private Clock ownerClock;
    private Logger logger;

    public MessageGenerator(int numProcesses, int ownerPid, Clock ownerClock) {
        this.numProcesses = numProcesses;
        this.ownerPid = ownerPid;
        this.ownerClock = ownerClock;
        logger = LogManager.getLogger(String.format("P%d", ownerPid));
    }

    private void broadcast() {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry();

        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        // create timestamp based own owner process clock
        int timestamp = ownerClock.createUpdate();
        logger.info(String.format("broadcasting %s", new Message(ownerPid, timestamp).toString()));

        for (int i = 0; i < numProcesses; i++) {
            try {
                // lookup process by its id and send message
                HW1Interface process = (HW1Interface) registry.lookup(String.valueOf(i));
//                System.out.println(String.format("p%d sending msg to p%d", ownerPid, i));
                process.addMessage(new Message(ownerPid, timestamp));
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                int rand = random.nextInt(5);
                Thread.sleep(rand * 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(String.format("Process %d alive and broadcasting", ownerPid));
            broadcast();
        }
    }
}
