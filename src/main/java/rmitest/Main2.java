package rmitest;

import ex2.ComponentImpl;
import ex2.ComponentInterface;
import ex2.ConfigReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Main2 {

    /**
     * this should do the following:
     * 1. setup the rmi registry
     * 2. run all components associated with hostname on different threads
     * @param args full qualified host name (needs to match with one in the config file)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("wrong number args");
            System.exit(1);
        }
        String hostName = args[0];


        /** create the registry */
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // for remote demo names need to be read in from file/args
        String[] names = new String[0];
        try {
            names = ConfigReader.getComponentNames();
        } catch (IOException e) {
            System.out.println("Error reading component names from config file");
            e.printStackTrace();
            System.exit(1);
        }
        String[] fullNames = ConfigReader.appendIdComponents(names);
        int numberProcesses = names.length;

        /**
         * only start up components that should be started on this host
         */
        // let process with id 0 be the one who initiates the algorithm
        if (names[0].equals(hostName))
            launchInThread(0, numberProcesses, fullNames, true);

        for (int i = 1; i < numberProcesses; i++) {
            if (names[i].equals(hostName))
                launchInThread(i, numberProcesses, fullNames, false);
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
                Naming.bind(names[id], stub);
                System.out.println(String.format("Component %s bound", String.valueOf(id)));
                component.start();

            } catch (RemoteException | MalformedURLException | AlreadyBoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

