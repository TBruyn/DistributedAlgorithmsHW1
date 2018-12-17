package ex3;

import java.io.Serializable;
import java.util.Objects;

public class Message implements Serializable {

    public Message(int level, int id, int sender) {
        this.level = level;
        this.id = id;
        this.sender = sender;
    }

    private int level;
    private int id;
    private int sender;

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public int getSender() {
        return sender;
    }

    public char compareTo(int otherLevel, int otherOwnerId) {
        if (level < otherLevel)
            return '<';
        if (level > otherLevel)
            return '>';
        if (id > otherOwnerId)
            return '>';
        if (id < otherOwnerId)
            return '<';
        return '=';
    }
}
