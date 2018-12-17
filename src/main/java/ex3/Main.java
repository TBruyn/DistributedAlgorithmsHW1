package ex3;

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
        setup(Integer.parseInt(args[0]));
    }

    public static void setup(int numberComponents) {
        /** create the registry */
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numberComponents; i++) {
            launchInThread(i, numberComponents);
        }

        System.out.println("launched all processes");
    }

    /**
     * launch component with id in new thread
     * @param id
     * @param n
     */
    private static void launchInThread(int id, int n) {
        new Thread(() -> {
            try {
                ComponentImpl component = new ComponentImpl(id, n);
                ComponentInterface stub = (ComponentInterface) UnicastRemoteObject.exportObject(component, 0);

                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(String.valueOf(id), stub);

                System.out.println(String.format("Process %s bound", String.valueOf(id)));
                component.start();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
