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
        for(int i = 0; i < numberComponents; i++) {
            RMIUtil.ordinarySend(new Message(level, id, id), i);

            while(true) {
                if (msgBuffer.isEmpty()) continue;
                Message msg = msgBuffer.remove();

                if (msg.getId() == id && !killed) {
                    level++;
                    break;
                } else {
                    if (msg.compareTo(level, id) == '<') {
                        continue;
                    } else {
                        RMIUtil.ordinarySend(msg, msg.getSender());
                        killed = true;
                        continue;
                    }
                }
            }

        }
        if (!killed) {
            //ELECTED
        }

    }

    public void receive(Message msg) {
        synchronized (this) {
            msgBuffer.add(msg);
        }
    }
}
