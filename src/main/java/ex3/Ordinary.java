package ex3;

import java.util.Map;

public class Ordinary {

    private int processId;
    private int level;
    private int ownerId;
    private int potentialFather;
    private int father;

    public Ordinary(int id) {
        processId = id;
        level = ownerId = potentialFather = father = -1;
    }

    public synchronized void receive(Message msg) {
        System.out.println(String.format("Ordinary %d received %s", processId, msg.toString()));
        switch (msg.compareTo(level, ownerId)) {
            case '<':
                log(Manager.RECEIVE_CAPTURE_REQUEST);
                break;
            case '>':
                log(Manager.RECEIVE_CAPTURE_REQUEST);
                potentialFather = msg.getSender();
                level = msg.getLevel();
                ownerId = msg.getId();
                if (father == -1) {
                    father = potentialFather;
                    log(Manager.SEND_CAPTURE_CONFIRM);
                    RMIUtil.candidateSend(msg.forward(processId), father);
                } else {
                    log(Manager.SEND_KILL_REQUEST);
                    RMIUtil.candidateSend(msg.forward(processId), father);
                }
                break;
            case '=':
                log(Manager.RECEIVE_KILL_CONFIRM);
                father = potentialFather;
                log(Manager.SEND_CAPTURE_CONFIRM);
                RMIUtil.candidateSend(msg.forward(processId), father);
                break;
        }
    }

    private void log(String key) {
        Map<String, Integer> messageData = Manager.getInstance().getMessageData().get(processId);
        messageData.replace(key, messageData.get(key) + 1);
    }

}
