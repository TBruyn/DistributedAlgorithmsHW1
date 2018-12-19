package ex3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIUtil {

    public static void candidateSend(Message m, int receiver) {
        DelayUtil.delay(50);
        try {
            getComponent(receiver).callCandidate(m);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("Could not candidate-send to %d", receiver));
        }
    }

    public static void ordinarySend(Message m, int receiver) {
        DelayUtil.delay(50);
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

    public static void terminateAll(int numberOfComponents) {
        // kill all components
        for (int component = 0; component < numberOfComponents; component++) {
            try {
                getComponent(component).terminate();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("could not terminate component %d", component));
            }
        }
    }

    private static ComponentInterface getComponent(int id) throws Exception {
        Registry registry = LocateRegistry.getRegistry();
        return (ComponentInterface) registry.lookup(String.valueOf(id));
    }


}
