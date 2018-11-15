public class Clock {

    /** access must be synchronized */
    private Timestamp time;
    private int ownerPid;

    public Clock() {

    }
    public void receiveUpdate(Message m) {

    }

    public Timestamp createUpdate() {
        return time;
    }
}
