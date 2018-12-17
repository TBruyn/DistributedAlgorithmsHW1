package ex3;

public class Ordinary {

    private int level;
    private int ownerId;
    private int potentialFather;
    private int father;

    public Ordinary() {
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
                RMIUtil.candidateSend(msg, father);
                break;
            case '=':
                father = potentialFather;
                RMIUtil.candidateSend(msg, father);
                break;
        }

    }

}
