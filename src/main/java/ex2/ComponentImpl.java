package ex2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static ex2.Parameters.CHANNEL_DELAY;
import static ex2.Parameters.ROUNDS_UNTIL_INIT;

public class ComponentImpl implements ComponentInterface {

    private static final int INCOMING = 0;
    private static final int OUTGOING = 1;
    private int id;
    private boolean algoInitiator;
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
    private boolean channelsBuffered;

    private Logger logger;

    private List<Map<Integer, int[]>> stateHistory;
    private List<Map<Integer,Queue<Integer>>> channelHistory;

    public ComponentImpl(int myId, int numberComponents, String[] names, boolean init) {
        id = myId;
        algoInitiator = init;
        this.numberComponents = numberComponents;
        namesOfComponents = names;
        localStateRecorded = false;
        localState = new HashMap<>();
        channelBuffers = new HashMap<>();
        channelsBuffered = false;
        incomingChannels = new ConcurrentHashMap<>();
        for (int i = 0; i < numberComponents; i++) {
            if (i != id) {
                incomingChannels.put(i, new LinkedList<>());
                channelBuffers.put(i, new LinkedList<>());
                int[] outIn = {0, 0};
                localState.put(i, outIn);
            }
        }
        logger = LogManager.getLogger(String.format("Comp%d", id));

        stateHistory = new ArrayList<>();
        channelHistory = new ArrayList<>();
    }

    @Override
    public void receive(int sender, int message) {
        Queue<Integer> channel = incomingChannels.get(sender);
        channel.add(message);
    }

    private void receiveMarker(int sender) {
        if (!localStateRecorded) {
            // state of channel from sender to self is empty
            recordChannelState(sender);
            sendMarker();
        }
        else {
            // state of channel from sender to self is associated buffer
            recordChannelState(sender);
        }
    }

    private void sendMarker() {
        recordLocalState();
        localStateRecorded = true;
        channelsBuffered = true;
        for (int i = 0; i < numberComponents; i++) {
            if (i != id) {
                send(i, MARKER);
            }
        }
    }

    private void recordLocalState() {
        logger.warn(String.format("LOCAL STATE component %d:", id));
        for (int i = 0; i < numberComponents; i++) {
            if (i != id) {
                logger.warn(String.format("- OUTGOING TO %d: %d", i, localState.get(i)[OUTGOING]));
                logger.warn(String.format("- INCOMING FROM %d: %d", i, localState.get(i)[INCOMING]));
            }
        }
        addStateToHistory();
    }

    private void recordChannelState(int sender) {
        StringBuilder builder = new StringBuilder(String.format("CHANNEL FROM %d to %d:", sender, id));
        for (Integer integer : channelBuffers.get(sender)) {
            builder.append(String.format(" %d", integer));
        }
        logger.warn(builder.toString());
        addChannelStateToHistory();
    }

    private void addStateToHistory() {
        if(stateHistory.size() < 10) {
            Map<Integer, int[]> stateCopy = new HashMap<>();
            for (Integer key : localState.keySet()) {
                stateCopy.put(key, localState.get(key));
            }
            stateHistory.add(stateCopy);
        }
    }

    private void addChannelStateToHistory() {
        if(channelHistory.size() < 10) {
            Map<Integer, Queue<Integer>> stateCopy = new HashMap<>();
            for (Integer key : channelBuffers.keySet()) {
                stateCopy.put(key, channelBuffers.get(key));
            }
            channelHistory.add(stateCopy);
        }
    }

    @Override
    public List<Map<Integer, int[]>> getStateHistory() {
        return stateHistory;
    }

    @Override
    public List<Map<Integer, Queue<Integer>>> getChannelHistory() {
        return channelHistory;
    }

    /**
     * send message to other component
     * @param receiver id of other component
     * @param message -1 if marker
     */
    protected void send(int receiver, int message) {
        // call receive on correct remote stub (get name from name array)
        try {
            ComponentInterface receiverComponent = (ComponentInterface) Naming.lookup(namesOfComponents[receiver]);
            receiverComponent.receive(id, message);
            // update local state
            localState.get(receiver)[OUTGOING] += 1;
            logger.info(String.format("sent message %d to comp%d", message, receiver));
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * start the components main loop
     */
    public void start() {
        DelayUtil.initialTimeout();
        int round = 0;
        while (true) {
            // check if self is initiator and time to intiate
            if (algoInitiator && round == ROUNDS_UNTIL_INIT)
                sendMarker();

            // send message to random receiver
            int receiver = randomOtherComponent();
            int state_outgoing = localState.get(receiver)[OUTGOING];
            int message = state_outgoing + 1;
            send(receiver, message);

            // incorporate delays
            DelayUtil.delay(CHANNEL_DELAY);
            // receive messages from channels and update local state
            deliverFromChannels();
            round++;
        }
    }

    /**
     * can be used to obtain id of randomly picked other component
     * @return
     */
    private int randomOtherComponent() {
        Random random = new Random();
        int receiver = id;
        while (receiver == id) {
            receiver = random.nextInt(numberComponents);
        }
        return receiver;
    }

    /**
     * deliver next message for all incoming channels
     * distinguish cases
     * 1: marker message
     * 2: non-marker and channel buffered
     * 3: non-marker and channel open
     */
    private void deliverFromChannels() {
        for (int i = 0; i < numberComponents; i++) {
            if (i != id) {
                Queue<Integer> channel = incomingChannels.get(i);
                Integer message;
                // TODO: check if really needed
                synchronized (this) {
                    message = channel.poll();
                }
                // if no message in channel : poll returns null
                if (message == null)
                    continue;
                // if marker start algorithm's procedure on receiving marker
                if (message == MARKER) {
                    receiveMarker(i);
                }
                // if normal message
                else {
                    // if channel open update local state
                    if (!channelsBuffered) {
                        localState.get(i)[INCOMING] += 1;
                        logger.info(String.format("received msg %d from comp%d", message, i));
                    }
                    // else append to channel buffer
                    else
                        channelBuffers.get(i).offer(message);
                }
            }
        }
    }
}
