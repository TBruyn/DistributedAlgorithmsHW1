package ex2;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {

    /**
     * this should do the following:
     * 1. setup the rmi registry
     * 2. run all processes on different JVMs (or threads)
     * @param args number of processes
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("wrong number args");
            System.exit(1);
        }
        int numberProcesses = Integer.parseInt(args[0]);

        /** create the registry */
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // for remote demo names need to be read in from file/args
        String[] names = new String[numberProcesses];
        for (int i = 0; i < numberProcesses; i++) {
            names[i] = String.format("rmi://localhost:1099/%d", i);
        }

        // let process with id 0 be the one who initiates the algorithm
        launchInThread(0, numberProcesses, names, true);
        for (int i = 1; i < numberProcesses; i++) {
                launchInThread(i, numberProcesses, names, false);
        }

        System.out.println("launched all processes");
    }


    /**
     * launch component with id in new thread
     * @param id
     * @param n
     * @param names
     */
    private static void launchInThread(int id, int n, String[] names, boolean init) {
        new Thread(() -> {
            try {
                ComponentImpl component = new ComponentImpl(id, n, names, init);
                ComponentInterface stub = (ComponentInterface) UnicastRemoteObject.exportObject(component, 0);
                // should be replaced by code that binds with full url name
//                Registry registry = LocateRegistry.getRegistry();
//                registry.rebind(String.valueOf(pid), stub);
                Naming.bind(names[id], stub);
                System.out.println(String.format("Process %s bound", String.valueOf(id)));
                component.start();

            } catch (RemoteException | MalformedURLException | AlreadyBoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
