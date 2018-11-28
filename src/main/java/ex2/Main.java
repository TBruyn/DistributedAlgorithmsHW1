package ex2;

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

        String[] names = null;
        for (int i = 0; i < numberProcesses; i++) {
            try {
                launchInThread(i, numberProcesses, names);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("some exception occurred when launching processes");
            }
        }

        System.out.println("launched all processes");
//        while (true) {}
    }


    /**
     * launch component with id in new thread
     * @param id
     * @param n
     * @param names
     */
    private static void launchInThread(int id, int n, String[] names) {
        new Thread(() -> {
            try {
                ComponentImpl component = new ComponentImpl(id, n, names);
                ComponentInterface stub = (ComponentInterface) UnicastRemoteObject.exportObject(component, 0);
                // should be replaced by code that binds with full url name
//                Registry registry = LocateRegistry.getRegistry();
//                registry.rebind(String.valueOf(pid), stub);
                System.out.println(String.format("Process %s bound", String.valueOf(id)));
                component.start();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
