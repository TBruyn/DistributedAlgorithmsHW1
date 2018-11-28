package ex1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HW1 implements HW1Interface{

    private final Logger logger;
    private Clock clock;
    private Buffer messageBuffer;
    private BlockingQueue<Message> pendingAcks;
    private int pid;
    private int numProcesses;

    /**
     * pid is process' own id and amount declares there exist processes 0..amount-1
     * @param pid
     * @param amount
     * @throws RemoteException
     */
    public HW1(int pid, int amount) throws RemoteException {
        this.pid = pid;
        this.numProcesses = amount;
        clock = new Clock(pid);
        messageBuffer = new Buffer(pid, numProcesses);
        pendingAcks = new LinkedBlockingQueue<>();
        logger = LogManager.getLogger(String.format("P%d", pid));
    }

    /**
     * method called by other processes (remotely) to send a message
     * @param m
     */
    @Override
    public void addMessage(Message m) {
        clock.receiveUpdate(m);
        pendingAcks.add(m);
        messageBuffer.addMessage(m);
    }

    /**
     * method called by other processes (remotely) to send ack
     * @param a
     */
    @Override
    public void addAck(Acknowledgement a) {
        messageBuffer.addAcknowledgement(a);
    }

    private void delay(int multiplier) {
        Random random = new Random();
        int rand = random.nextInt(5);
        try {
            Thread.sleep(rand * multiplier);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initialTimeout() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> deliveredMessages() {
        return messageBuffer.getDeliveredMessages();
    }

    /**
     * this should start the generation of messages of the process:
     * the client-side work of it
     */
    public void startProcess() {
        initialTimeout();
        while (true) {
            broadcast();
        }
    }

    /**
     * broadcast a new message to all other processes
     * !! make sure all pending acks are sent before
     */
    private void broadcast() {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry();

        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        List<Message> acks = new ArrayList<>();
        int timestamp;
        // make sure we acknowledge all older incoming messages but no newer incoming ones
        // new incoming ones will only be added after creating the message
        synchronized (this) {
            // flush acks
            pendingAcks.drainTo(acks);
            // create timestamp based own owner process clock
            timestamp = clock.createUpdate();
        }

        // acknowledge all earlier messages (than the one created)
        for (Message m : acks) {
            delay(Constants.ACK_DELAY);
            sendAcknowledgement(m);
        }

        // now safe to send the message to all other processes
        Message m = new Message(pid, timestamp);
        logger.info(String.format("start broadcasting msg %s", m.toString()));
        for (int i = 0; i < numProcesses; i++) {
            // simulate delays of messages
            delay(Constants.BROADCAST_DELAY);
            try {
                // lookup process by its id and send message
                HW1Interface process = (HW1Interface) registry.lookup(String.valueOf(i));
//                System.out.println(String.format("p%d sending msg to p%d", ownerPid, i));
                process.addMessage(m);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
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
        for (int i = 0; i < numProcesses; i++) {
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

    /**
     * the main method sets up the process to run on its jvm
     * this includes creating the process object, registering it and tell it to start transmitting messages
     * @param args two integers: pid of process to be set up and the total amount of processes
     */
    public static void main(String[] args) {
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
        String name = args[0];
        String numProcs = args[1];
        int pid = Integer.parseInt(name);
        int numP = Integer.parseInt(numProcs);

        try {
            HW1 process = new HW1(pid, numP);
            HW1Interface stub = (HW1Interface) UnicastRemoteObject.exportObject(process, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println(String.format("Process %s bound", name));
            process.startProcess();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
