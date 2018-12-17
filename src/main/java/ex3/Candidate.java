package ex3;

import java.util.LinkedList;
import java.util.Queue;

public class Candidate {

    private int level;
    private int numberComponents;
    private int id;
    private boolean killed;
    private Queue<Message> msgBuffer;


    public Candidate(int numberComponents, int id) {
        this.numberComponents = numberComponents;
        this.id = id;
        level = 0;
        killed = false;
        msgBuffer = new LinkedList<>();
    }

    public void start() {
        // algo for candidate

    }

    public void receive(Message msg) {
        synchronized (this) {
            msgBuffer.add(msg);
        }
    }
}
