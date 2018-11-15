public class MessageGenerator implements Runnable{
    private int numProcesses;
    private int ownerPid;
    private Clock ownerClock;

    public MessageGenerator(int numProcesses, int ownerPid, Clock ownerClock) {
        this.numProcesses = numProcesses;
        this.ownerPid = ownerPid;
        this.ownerClock = ownerClock;
    }

    @Override
    public void run() {

    }
}
