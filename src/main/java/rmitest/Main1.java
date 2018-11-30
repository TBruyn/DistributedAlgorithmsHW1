package rmitest;

import ex1.HW1;
import ex1.HW1Interface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * this is the Main class for demoing exercise 1 on one host but different processes
 */
public class Main1 {

    /**
     * this should do the following:
     * create registry (will fail for everyone but the first invokation)
     * run all processes on different threads
     * @param args number of processes, first id, last id (of processes on this host)
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("wrong number args");
            System.exit(1);
        }

        /** create the registry */
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int numberProcesses = Integer.parseInt(args[0]);
        int firstP = Integer.parseInt(args[1]);
        int lastP = Integer.parseInt(args[2]);

        List<Integer> localPs = new ArrayList<>();
        List<Integer> remotePs = new ArrayList<>();

        for (int i = 0; i < numberProcesses; i++) {
            if (i <= lastP && i>= firstP)
                localPs.add(i);
            else
                remotePs.add(i);
        }

        for (int p: localPs) {
            launchInThread(p, numberProcesses);
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
