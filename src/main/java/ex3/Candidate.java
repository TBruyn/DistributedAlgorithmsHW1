package ex3;

import java.util.LinkedList;
import java.util.Queue;

public class Candidate {

    private int level;
    private int numberComponents;
    private int id;
    private boolean killed;
    private Queue<Message> msgBuffer;
    private boolean running;


    public Candidate(int numberComponents, int id) {
        this.numberComponents = numberComponents;
        this.id = id;
        level = 0;
        killed = false;
        msgBuffer = new LinkedList<>();
        running = true;
    }

    public void start() {
        for(int i = 0; i < numberComponents; i++) {
            if (!running) return;
            RMIUtil.ordinarySend(new Message(level, id, id), i);

            while(true) {
                if (!running) return;
                if (msgBuffer.isEmpty()) continue;
                Message msg = msgBuffer.remove();

                if (msg.getId() == id && !killed) {
                    level++;
                    break;
                } else {
                    if (msg.compareTo(level, id) == '<') {
                        continue;
                    } else {
                        RMIUtil.ordinarySend(msg.forward(id), msg.getSender());
                        killed = true;
                        continue;
                    }
                }
            }

        }
        if (!killed) {
            // report winner of election and terminate algorithm
            Manager.getInstance().announceElection(id);
            RMIUtil.terminateAll(numberComponents);
        }

    }

    public synchronized void receive(Message msg) {
            msgBuffer.add(msg);
    }

    public void terminate() {
        running = false;
        //TODO manager logging max level
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                '}';
    }
}
