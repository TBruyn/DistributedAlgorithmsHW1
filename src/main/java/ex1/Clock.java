package ex1;

public class Clock {

    /** access must be synchronized */
    private int time;
    private int ownerPid;

    public Clock(int pid) {
        time = 0;
        ownerPid = pid;
    }

    public synchronized void receiveUpdate(Message m) {
        time = Math.max(m.getTimestamp(), time) + 1;
    }

    public synchronized int createUpdate() {
        time += 1;
        return time;
    }

    public int getOwnerPid() {
        return ownerPid;
    }
}
