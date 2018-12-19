package ex3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private static Manager ourInstance = new Manager();
    private boolean[] terminated;
    private int winnerId;

    public static Manager getInstance() {
        return ourInstance;
    }

    private List<Map<String, Integer>> data;

    private Manager() {
    }

    public void init(int numberComponents) {
        data = new ArrayList<>(numberComponents);
        winnerId = -1;
        terminated = new boolean[numberComponents];
        for (int i = 0; i < numberComponents; i++) {
            Map<String, Integer> map = new HashMap<>();
            //// ordinary
            // capture receives
            // killed receives
            // send kill
            // send confirmation (number of captures)
            //// canditate
            // level reached
            data.add(map);
        }
    }

    public void logMessage(int ordinaryId, Message msg, int recipient) {

    }

    public void logLevel(int candidateId, int level) {

    }

    public void announceTermination(int componentId) {
        terminated[componentId] = true;
    }

    public void announceElection(int id) {
        winnerId = id;
    }

    public boolean allTerminated() {
        for (boolean b : terminated) {
            if (!b) return false;
        }
        return true;
    }
}
