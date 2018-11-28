package ex1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainRemote {

    private static final String HOST_ADDRESS = "rmi://localhost:1099";
    private static final String REMOTE_ADDRESS = "";

    /**
     * class for demonstration on two computes
     * this should do the following:
     * 1. setup the rmi registry
     * 2. run all processes on different threads
     * @param args even number of processes, int (0: if first part, 1: if second part)
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("wrong number args");
        }
        int numberProcesses = Integer.parseInt(args[0]);
        boolean firstHalf = false;
        if (Integer.parseInt(args[1]) == 1)
            firstHalf = true;

        /** create the registry */
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int first = 0;
        int last = numberProcesses - 1;

        if (firstHalf)
            last = numberProcesses / 2 - 1;
        else
            first = numberProcesses / 2;

        for (int i = first; i < last; i++) {
            try {
                launchInThread(i, numberProcesses);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("some exception occurred when launching processes");
            }
        }

        System.out.println("launched all processes");
//        while (true) {}
    }



    /**
     * launch process in the same jvm in another thread
     * @param pid
     * @param n
     */
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
