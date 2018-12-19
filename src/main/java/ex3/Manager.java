package ex3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    public static final String RECEIVE_CAPTURE_REQUEST = "rcr";
    public static final String SEND_CAPTURE_CONFIRM = "rcc";
    public static final String SEND_KILL_REQUEST = "rkr";
    public static final String RECEIVE_KILL_CONFIRM = "rkc";

    private static Manager ourInstance = new Manager();
    private boolean[] terminated;
    private int winnerId;

    public static Manager getInstance() {
        return ourInstance;
    }

    private List<Map<String, Integer>> messageData;

    private Map<Integer, Integer> maxLevels;

    private Manager() {
    }

    public void init(int numberComponents) {
        messageData = new ArrayList<>(numberComponents);
        maxLevels = new HashMap<>();
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
            messageData.add(map);


        }
    }

    public synchronized void logLevel(int candidateId, int level) {
        maxLevels.put(candidateId, level);
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

    public int getWinnerId() {
        return winnerId;
    }

    public List<Map<String, Integer>> getMessageData() {
        return messageData;
    }

    public Map<Integer, Integer> getMaxLevels() {
        return maxLevels;
    }
}
