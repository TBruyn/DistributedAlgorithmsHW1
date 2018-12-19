package ex3;

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

    public void receive(Message msg) {
        switch (msg.compareTo(level, ownerId)) {
            case '<':
                break;
            case '>':
                potentialFather = msg.getSender();
                level = msg.getLevel();
                ownerId = msg.getId();
                if (father == -1) father = potentialFather;
                RMIUtil.candidateSend(msg.forward(processId), father);
                break;
            case '=':
                father = potentialFather;
                RMIUtil.candidateSend(msg.forward(processId), father);
                break;
        }

    }

}
