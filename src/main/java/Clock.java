public class Clock {

    /** access must be synchronized */
    private int counter;
    private int time;
    private int ownerPid;

    public Clock() {

    }
    public void receiveUpdate(Message m) {

    }

    public int createUpdate() {
        return time;
    }
}
