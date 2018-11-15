import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class HW1 implements HW1Interface{

    private Clock clock;
    private Buffer messageBuffer;
    private int pid;
    private int numProcesses;
    private Registry registry;

    /**
     * pid is process' own id and amount declares there exist processes 0..amount-1
     * @param pid
     * @param amount
     * @throws RemoteException
     */
    public HW1(int pid, int amount) throws RemoteException {
        this.pid = pid;
        this.numProcesses = amount;
        registry = LocateRegistry.getRegistry();
        clock = new Clock();
        messageBuffer = new Buffer(numProcesses);
    }

    @Override
    public void addMessage(Message m) {
        messageBuffer.addMsg(m);
        clock.receiveUpdate(m);
    }

    @Override
    public void addAck(Acknowledgement a) {
        messageBuffer.addAck(a);
    }

    /**
     * this should start the generation of messages of the process:
     * the client-side work of it
     */
    private void startProcess() {
        new MessageGenerator(numProcesses, pid, clock).run();
    }

    /**
     * the main method sets up the process to run on its jvm
     * this includes creating the process object, registering it and tell it to start transmitting messages
     * @param args two integers: pid of process to be set up and the total amount of processes
     */
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        String name = args[0];
        String numProcs = args[1];
        int pid = Integer.getInteger(name);
        int numP = Integer.getInteger(numProcs);

        try {
            HW1 process = new HW1(pid, numP);
            HW1 stub = (HW1) UnicastRemoteObject.exportObject(process, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println(String.format("Process %s bound", name));
            process.startProcess();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
