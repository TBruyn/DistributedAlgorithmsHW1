package ex2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentImpl implements ComponentInterface {

    private static final int INCOMING = 0;
    private static final int OUTGOING = 1;
    private int id;
    private int numberComponents;
    // the names should be the full rmi url
    private String[] namesOfComponents;
    private boolean localStateRecorded;
    /**
     * only accessed from one thread: local state (?) and channel buffers
     * could be read from multiple threads: incoming channels map
     * could be written to and read from two different threads: single channel
     */
    private Map<Integer, int[]> localState;
    private Map<Integer, Queue<Integer>> incomingChannels;
    private Map<Integer, Queue<Integer>> channelBuffers;

    public ComponentImpl(int myId, int numberComponents, String[] names) {
        id = myId;
        this.numberComponents = numberComponents;
        namesOfComponents = names;
        localStateRecorded = false;
        localState = new HashMap<>();
        channelBuffers = new HashMap<>();
        incomingChannels = new ConcurrentHashMap<>();
        for (int i = 0; i < numberComponents; i++) {
            if (i != id) {
                incomingChannels.put(i, new LinkedList<>());
                int[] outIn = {0, 0};
                localState.put(i, outIn);
            }
        }
    }

    @Override
    public void receive(int sender, int message) {
        Queue<Integer> channel = incomingChannels.get(sender);
        channel.add(message);
    }

    private void receiveMarker(int sender) {

    }

    private void sendMarker() {
        
    }

    protected void send(int receiver) {
        // call receive on correct remote stub (get name from name array)
        // update local state
    }

    protected void start() {
        // incorporate delays
        // receive messages from channels and update local state
        // start procedure on encountering marker
    }
}
