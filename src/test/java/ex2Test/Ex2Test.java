package ex2Test;

import ex2.ComponentImpl;
import ex2.ComponentInterface;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex2Test {

    @Test
    void test() {
        int numberProcesses = 3;

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

        //Let processes run
        try {
            Thread.sleep(60*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Collect results
        List[][] allStates = retrieveStates(names, numberProcesses);

        for (int i = 0; i < numberProcesses; i++) {
            List<Map<Integer, int[]>> stateHistoryOfProcess = (List<Map<Integer, int[]>>) allStates[i][0];
            Map<Integer, Queue<Integer>> channelHistoryOfProcess =
                    (Map<Integer, Queue<Integer>>) allStates[i][1];
        }

    }
    private int out = 0;
    private int in = 1;

    private void checkConsistency(
            HashMap<Integer, int[]>[] localStates,
            Map<Integer, Queue<Integer>>[] channelStates) {
        for (int pid = 0; pid < localStates.length; pid++) {
            HashMap<Integer, int[]> localState = localStates[pid];
            for (int oid = pid + 1; oid < localStates.length; oid++) {
                HashMap<Integer, int[]> otherState = localStates[oid];
                int[] otherComm = otherState.get(pid);
                int[] myComm = localState.get(oid);
                Queue<Integer> channelToMe = channelStates[pid].get(oid);
                Queue<Integer> channelToOther = channelStates[oid].get(pid);
                if (channelToMe.isEmpty()) {
                    assertEquals(otherComm[out], myComm[in]);
                } else {
                    int last = channelToMe.poll();
                    assertEquals(last - 1, myComm[in]);

                    while(! channelToMe.isEmpty())
                        last = channelToMe.poll();
                    assertEquals(last, otherComm[out]);
                }
            }
        }
    }

    private List[][] retrieveStates(String[] names, int numberProcesses) {
        List[][] allStates = new List[numberProcesses][2];
        for (int i = 0; i < numberProcesses; i++) {
            try {
                ComponentInterface receiverComponent = (ComponentInterface)
                        Naming.lookup(names[i]);
                List<Map<Integer, int[]>> stateHistory = receiverComponent.getStateHistory();
                List<Map<Integer, Queue<Integer>>> channelHistory = receiverComponent.getChannelHistory();

            } catch (NotBoundException | MalformedURLException | RemoteException e) {
                e.printStackTrace();
            }
        }
        return allStates;
    }

    private static void launchInThread(int id, int n, String[] names, boolean
            init) {
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
