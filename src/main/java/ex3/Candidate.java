package ex3;

import java.util.*;

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
        List<Integer> uncapturedComponents = new ArrayList<>(numberComponents);
        for(int i = 0; i < numberComponents; i++) {
            uncapturedComponents.add(i,i);
        }
        Collections.shuffle(uncapturedComponents);
        System.out.println(id + ": " + uncapturedComponents.toString());

        for(int i : uncapturedComponents) {
            if (!running) return;
            RMIUtil.ordinarySend(new Message(level, id, id), i);

            while(true) {
                DelayUtil.delay(10);
                if (!running) return;
                if (msgBuffer.isEmpty()) continue;
                Message msg = msgBuffer.remove();

                if (msg.getId() == id && !killed) {
                    level++;
                    System.out.println("Process " + id + " has level " + level);
                    break;
                } else {
                    if (msg.compareTo(level, id) == '<') {
                        System.out.println("Process " + id + " discards " + msg.toString() + " because it is" + " lower than " + level + ", " + id);
                        continue;
                    } else {
                        System.out.println("Process " + id + " is killed");
                        RMIUtil.ordinarySend(msg.forward(id), msg.getSender());
                        killed = true;
                        continue;
                    }
                }
            }

        }
        if (!killed) {
            // report winner of election and terminate algorithm
            System.out.println("Process " + id + " wins");
            Manager.getInstance().announceElection(id);
            RMIUtil.terminateAll(numberComponents);
        }

    }

    public synchronized void receive(Message msg) {
            msgBuffer.add(msg);
    }

    public void terminate() {
        running = false;
        Manager.getInstance().logLevel(id, level);
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                '}';
    }
}
