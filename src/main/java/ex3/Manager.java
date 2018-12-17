package ex3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private static Manager ourInstance = new Manager();

    public static Manager getInstance() {
        return ourInstance;
    }

    private List<Map<String, Integer>> data;

    private Manager() {
    }

    public void init(int numberComponents) {
        data = new ArrayList<>(numberComponents);
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

    public void announceElection(int id) {

    }
}
