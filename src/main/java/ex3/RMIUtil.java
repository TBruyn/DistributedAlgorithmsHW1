package ex3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIUtil {

    public static void candidateSend(Message m, int receiver) {
        try {
            getComponent(receiver).callCandidate(m);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("Could not candidate-send to %d", receiver));
        }
    }

    public static void ordinarySend(Message m, int receiver) {
        try {
            getComponent(receiver).callOrdinary(m);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("Could not ordinary-send to %d", receiver));
        }
    }

    public static void startCandidate(int id) {
        try {
            getComponent(id).startCandidate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("Could not start candidate %d", id));
        }
    }

    private static ComponentInterface getComponent(int id) throws Exception {
        Registry registry = LocateRegistry.getRegistry();
        return (ComponentInterface) registry.lookup(String.valueOf(id));
    }

    private static void terminateAll(int numberOfComponents) {
        //TODO kill everybody
    }


}
