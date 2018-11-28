package ex1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;

public class HW1 implements HW1Interface{

    private Clock clock;
    private Buffer messageBuffer;
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
    }

    @Override
    public void addMessage(Message m) {
//        System.out.println(String.format("p%d receives %s", pid, m.toString()));
//        sleepy();
        clock.receiveUpdate(m);
        messageBuffer.addMessage(m);
    }

    @Override
    public void addAck(Acknowledgement a) {
//        sleepy();
        messageBuffer.addAcknowledgement(a);
    }

    private void sleepy() {
        Random random = new Random();
        int rand = random.nextInt(5);
        try {
            Thread.sleep(rand * 500);
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
        // sleep to ensure that all processes are bound
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new MessageGenerator(numProcesses, pid, clock)).start();
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
